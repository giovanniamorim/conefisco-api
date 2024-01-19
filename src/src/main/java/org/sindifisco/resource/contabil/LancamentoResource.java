package org.conefisco.resource.contabil;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.conefisco.model.FileDB;
import org.conefisco.model.Lancamento;
import org.conefisco.model.LancamentoAgrupado;
import org.conefisco.repository.fileDB.FileDBRepository;
import org.conefisco.repository.lancamento.LancamentoRepository;
import org.conefisco.repository.contabil.planoContas.PlanoContasRepository;
import lombok.extern.slf4j.Slf4j;
import org.conefisco.repository.filter.LancamentoFilter;
import org.conefisco.service.FileDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.Table;
import javax.validation.Valid;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@Table(name = "ctb_lancamento")
@RequestMapping("/api/lancamentos")
@Slf4j
public class LancamentoResource {
    @Autowired
    LancamentoRepository lancamentoRepository;
    Pageable unpaged = Pageable.unpaged();


    @Autowired
    private PlanoContasRepository planoContasRepository;

    @Autowired
    private FileDBService fileDBService;

    @Autowired
    private FileDBRepository fileDBRepository;

    @PostMapping()
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('ROLE_CREATE') and #oauth2.hasScope('write')")
    public Lancamento addLancamento(@RequestBody @Valid Lancamento lancamento) {
        return lancamentoRepository.save(lancamento);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Lancamento findById(@PathVariable Long id) {
        return lancamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Lançamento não encontrado"));
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Page<Lancamento> getAll(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable){
        return lancamentoRepository.findAll(pageable);
    }


    @GetMapping("/receitas")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Page<Lancamento> getAllReceitas(
             Pageable pageable){
        return lancamentoRepository.findByTipoLancamento("Receita", pageable);
    }


    @GetMapping("/despesas")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Page<Lancamento> getAllDespesas(
            @PageableDefault(page = 0, size = 5, sort = "dataLancamento, desc", direction = Sort.Direction.DESC) Pageable pageable){
        return lancamentoRepository.findByTipoLancamento("Despesa", pageable);
    }

    @GetMapping("/busca-agrupada")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public ResponseEntity<?> buscaAvancadaAgrupada(
            @RequestParam(name = "groupByMes", defaultValue = "false") boolean agruparPorMes,
            @PageableDefault(page = 0, size = 5, sort = "dataLancamento", direction = Sort.Direction.DESC)
            LancamentoFilter lancamentoFilter, Pageable pageable) {

        if (agruparPorMes) {
            Page<Lancamento> lancamentosAgrupados = agruparLancamentosPorMes(lancamentoFilter, pageable);
            Double totalValor = lancamentoRepository.sumValorByFilter(lancamentoFilter);
            BuscaAvancadaResponse response = new BuscaAvancadaResponse((Page<Lancamento>) lancamentosAgrupados.getContent(), totalValor);
            return ResponseEntity.ok(response);
        }

        Page<Lancamento> lancamentos = lancamentoRepository.filtrar(lancamentoFilter, pageable);

        Double totalValor = lancamentoRepository.sumValorByFilter(lancamentoFilter);

        BuscaAvancadaResponse response = new BuscaAvancadaResponse((Page<Lancamento>) lancamentos.getContent(), totalValor);
        return ResponseEntity.ok(response);
    }


    // Função auxiliar para calcular o total dos valores
    private Double calculateTotal(List<Lancamento> lancamentos) {
        return lancamentos.stream().mapToDouble(Lancamento::getValor).sum();
    }



    @GetMapping("/busca")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public ResponseEntity<BuscaAvancadaResponse> buscaAvancada(
            @PageableDefault(page = 0, size = 5, sort = "dataLancamento", direction = Sort.Direction.DESC)
            LancamentoFilter lancamentoFilter, Pageable pageable) {

        Page<Lancamento> lancamentos = lancamentoRepository.filtrar(lancamentoFilter, pageable);
        Double totalValor = lancamentoRepository.sumValorByFilter(lancamentoFilter);

        BuscaAvancadaResponse response = new BuscaAvancadaResponse(lancamentos, totalValor);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/relatorio")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public ResponseEntity<byte[]> relatorio(LancamentoFilter lancamentoFilter) throws JRException {

        List<Lancamento> lancamentos = lancamentoRepository.filtrarRelatorio(lancamentoFilter);

        // Carregue o template do relatório
        InputStream jasperStream = getClass().getResourceAsStream("/reports/lancamentos.jasper");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        // Preencha o relatório com os dados simulados
        JasperPrint report = JasperFillManager.fillReport(jasperReport, null, new JRBeanCollectionDataSource(lancamentos));

        // Exporte o relatório para PDF
        byte[] data = JasperExportManager.exportReportToPdf(report);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=lancamentos.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);
    }

    @GetMapping("/relatorio-agrupado")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public ResponseEntity<byte[]> relatorioAgrupado(LancamentoFilter lancamentoFilter) throws JRException {
        try {
            List<Lancamento> lancamentos = lancamentoRepository.filtrarRelatorio(lancamentoFilter);

            // Group the entries by month and year, and calculate the sum of values
            Map<String, Double> somaValoresPorMes = new LinkedHashMap<>();
            for (Lancamento lancamento : lancamentos) {
                String mesAno = getMesAno(lancamento.getDataLancamento());
                somaValoresPorMes.merge(mesAno, lancamento.getValor(), Double::sum);
            }

            // Create a list of grouped data objects
            List<LancamentoAgrupado> lancamentosAgrupados = new ArrayList<>();
            for (Map.Entry<String, Double> entry : somaValoresPorMes.entrySet()) {
                LancamentoAgrupado lancamentoAgrupado = new LancamentoAgrupado();
                lancamentoAgrupado.setMesAno(entry.getKey());
                lancamentoAgrupado.setValorTotal(entry.getValue());
                lancamentosAgrupados.add(lancamentoAgrupado);
            }

            // Load the JasperReports template
            InputStream jasperStream = getClass().getResourceAsStream("/reports/lancamentos_agrupados.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

            // Fill the report with grouped data
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lancamentosAgrupados);
            JasperPrint report = JasperFillManager.fillReport(jasperReport, null, dataSource);

            // Export the report to PDF
            byte[] data = JasperExportManager.exportReportToPdf(report);

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=lancamentos-agrupados.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(data);
        } catch (Exception e) {
            // Handle exceptions and return appropriate response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Utility method to format date to month/year string
    private String getMesAno(LocalDate date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        return sdf.format(date);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_DELETE') and #oauth2.hasScope('write')")
    public ResponseEntity<String> deleteLancamento(@PathVariable Long id) {
        try {
            // Verificar se o lançamento existe
            Lancamento lancamento = lancamentoRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Lançamento não encontrado"));

            // Deletar o arquivo associado ao lançamento, caso exista
            String urlArquivo = lancamento.getFileUrl();
            if (urlArquivo != null && urlArquivo.contains("=")) {
                String nomeArquivo = urlArquivo.substring(urlArquivo.lastIndexOf("=") + 1);
                FileDB existingFile = fileDBRepository.findByName(nomeArquivo);
                if (existingFile != null) {
                    fileDBService.delete(existingFile);
                }
            }

            // Deletar o lançamento
            lancamentoRepository.delete(lancamento);

            return ResponseEntity.noContent().build(); // Resposta indicando sucesso
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build(); // Resposta indicando que o lançamento não foi encontrado
        } catch (Exception e) {
            String message = "Ocorreu um erro ao deletar o lançamento.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message); // Resposta indicando erro interno
        }
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE') and #oauth2.hasScope('write')")
    public ResponseEntity<Lancamento> updateLancamento(@PathVariable Long id, @RequestBody Lancamento updatedLancamento) {
        return lancamentoRepository.findById(id)
                .map(lancamento -> {
                    lancamento.setTipoLancamento(updatedLancamento.getTipoLancamento());

                    // Converter a data para UTC
                    LocalDate utcDate = updatedLancamento.getDataLancamento();
                    ZonedDateTime utcDateTime = utcDate.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime().toZonedDateTime();
                    lancamento.setDataLancamento(utcDateTime.toLocalDate());

                    lancamento.setPlanoConta(updatedLancamento.getPlanoConta());
                    lancamento.setValor((updatedLancamento.getValor()));
                    lancamento.setModoPagamento(updatedLancamento.getModoPagamento());
                    lancamento.setTipoComprovante(updatedLancamento.getTipoComprovante());
                    lancamento.setNumDoc(updatedLancamento.getNumDoc());
                    lancamento.setNumCheque(updatedLancamento.getNumCheque());
                    lancamento.setObs(updatedLancamento.getObs());
                    lancamento.setSupCaixa(updatedLancamento.getSupCaixa());
                    lancamento.setAnoExercicio(updatedLancamento.getAnoExercicio());
                    lancamento.setFileUrl(updatedLancamento.getFileUrl());

                    Lancamento putLancamento = lancamentoRepository.save(lancamento);

                    return ResponseEntity.ok().body(putLancamento);
                }).orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Lançamento não encontrado"));
    }


    private Page<Lancamento> agruparLancamentosPorMes(LancamentoFilter lancamentoFilter, Pageable pageable) {
        Page<Lancamento> lancamentos = lancamentoRepository.filtrar(lancamentoFilter, pageable);

        Map<String, List<Lancamento>> lancamentosAgrupados = lancamentos.getContent()
                .stream()
                .collect(Collectors.groupingBy(lancamento ->
                        YearMonth.from(lancamento.getDataLancamento()).toString()));

        List<Lancamento> lancamentosAgrupadosList = lancamentosAgrupados.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return new PageImpl<>(lancamentosAgrupadosList, pageable, lancamentosAgrupadosList.size());
    }


}