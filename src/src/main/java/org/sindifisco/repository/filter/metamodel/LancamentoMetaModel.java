package org.conefisco.repository.filter.metamodel;

import org.conefisco.model.Lancamento;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Lancamento.class)
public class LancamentoMetaModel {

    public static volatile SingularAttribute<Lancamento, Long> id;
    public static volatile SingularAttribute<Lancamento, String> tipoLancamento;
    public static volatile SingularAttribute<Lancamento, LocalDate> data;
    public static volatile SingularAttribute<Lancamento, Double> valor;

}
