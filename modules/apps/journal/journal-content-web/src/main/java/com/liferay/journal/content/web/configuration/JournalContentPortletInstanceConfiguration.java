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

package com.liferay.journal.content.web.configuration;

import aQute.bnd.annotation.metatype.Meta;

/**
 * @author Juergen Kappler
 */
@Meta.OCD(
	id = "com.liferay.journal.content.web.configuration.JournalContentPortletInstanceConfiguration"
)
public interface JournalContentPortletInstanceConfiguration {

	@Meta.AD(deflt = "", required = false)
	public String articleId();

	@Meta.AD(deflt = "", required = false)
	public String contentMetadataAssetAddonEntryKeys();

	@Meta.AD(deflt = "", required = false)
	public String ddmTemplateKey();

	@Meta.AD(required = false)
	public String enableViewCountIncrement();

	@Meta.AD(deflt = "0", required = false)
	public long groupId();

	@Meta.AD(deflt = "", required = false)
	public String userToolAssetAddonEntryKeys();

}