/* 
 * Copyright(c)2010-2014 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
 */ 
package com.wudaosoft.commons.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.google.common.collect.Lists;

/**
 * @author Changsoul Wu
 *
 */
public class DynamicSpecifications {

	public static <T> Specification<T> bySearchFilter(final Collection<SearchFilter> filters, final Class<T> entityClazz) {
		
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if ((filters != null) && !(filters.isEmpty())) {

					List<Predicate> predicates = Lists.newArrayList();
					
					for(SearchFilter filter : filters) {
						Predicate p = buildPredicate(filter, root, builder);
						
						if(p != null)
							predicates.add(p);
					}
					
					// 将所有条件用 and 联合起来
					if (!predicates.isEmpty()) {
						return builder.and(predicates.toArray(new Predicate[predicates.size()]));
					}
				}

				return builder.conjunction();
			}
		};
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static Predicate buildPredicate(final SearchFilter filter, final Root<?> root, final CriteriaBuilder builder) {

		// nested path translate, 如Task的名为"user.name"的filedName,
		// 转换为Task.user.name属性
		String[] names = StringUtils.split(filter.fieldName, ".");
		Path expression = root.get(names[0]);
		for (int i = 1; i < names.length; i++) {
			expression = expression.get(names[i]);
		}
		
		Predicate predicate = null;

		// logic operator
		switch (filter.operator) {
		case EQ:
			predicate = builder.equal(expression, filter.value);
			break;
		case LIKE:
			predicate = builder.like(expression, "%" + filter.value + "%");
			break;
		case GT:
			predicate = builder.greaterThan(expression, (Comparable) filter.value);
			break;
		case LT:
			predicate = builder.lessThan(expression, (Comparable) filter.value);
			break;
		case GTE:
			predicate = builder.greaterThanOrEqualTo(expression, (Comparable) filter.value);
			break;
		case LTE:
			predicate = builder.lessThanOrEqualTo(expression, (Comparable) filter.value);
			break;
		case IN:
			if (filter.value instanceof Collection<?>) {

				Collection<Object> colle = (Collection<Object>) filter.value;

				if (!colle.isEmpty()) {
					In<Object> in = builder.in(expression);

					Iterator<Object> iter = colle.iterator();

					while (iter.hasNext()) {
						in.value(iter.next());
					}

					predicate = in;
				}
			}
			break;
		case OR:

			if (filter.value instanceof OrFilter) {
				final List<Predicate> ps = new ArrayList<Predicate>();
				
				Iterator<SearchFilter> iter = ((OrFilter)filter.value).getSearchFilters().iterator();
				
				while (iter.hasNext()) {
					Predicate p = buildPredicate(iter.next(), root, builder);
					if(p != null)
						ps.add(p);
				}
				
				if(!ps.isEmpty())
					predicate = builder.or(ps.toArray(new Predicate[ps.size()]));
			}
			break;
		default:
			break;
		}
		
		return predicate;
	}
}
