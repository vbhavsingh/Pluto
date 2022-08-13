package com.log.server.util;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.log.server.LocalConstants;

public class NotHsqlBackendCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		return !LocalConstants.DATABASES.isHsqlDB();
	}

}
