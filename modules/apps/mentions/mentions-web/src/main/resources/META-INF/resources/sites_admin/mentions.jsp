<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/init.jsp" %>

<%
boolean groupMentionsEnabled = GetterUtil.getBoolean(request.getAttribute(MentionsWebKeys.GROUP_MENTIONS_ENABLED));
boolean companyMentionsEnabled = GetterUtil.getBoolean(request.getAttribute(MentionsWebKeys.COMPANY_MENTIONS_ENABLED));
%>

<h3><liferay-ui:message key="mentions" /></h3>

<c:choose>
	<c:when test="<%= companyMentionsEnabled %>">
		<aui:input checked="<%= groupMentionsEnabled %>" label='<%= LanguageUtil.get(resourceBundle, "allow-users-to-mention-other-users") %>' name="TypeSettingsProperties--mentionsEnabled--" type="checkbox" value="<%= groupMentionsEnabled %>" />
	</c:when>
	<c:otherwise>
		<div class="alert alert-warning">
			<liferay-ui:message key="mentions-are-disabled-in-portal-settings" />
		</div>
	</c:otherwise>
</c:choose>