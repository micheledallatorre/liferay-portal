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

package com.liferay.bookmarks.asset;

import com.liferay.bookmarks.constants.BookmarksPortletKeys;
import com.liferay.bookmarks.constants.BookmarksWebKeys;
import com.liferay.bookmarks.model.BookmarksFolder;
import com.liferay.bookmarks.service.BookmarksEntryServiceUtil;
import com.liferay.bookmarks.service.BookmarksFolderServiceUtil;
import com.liferay.bookmarks.service.permission.BookmarksFolderPermissionChecker;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.trash.TrashRenderer;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.asset.model.AssetRendererFactory;
import com.liferay.portlet.asset.model.BaseJSPAssetRenderer;
import com.liferay.portlet.trash.util.TrashUtil;

import java.util.Date;
import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Eudaldo Alonso
 * @author Alexander Chow
 */
public class BookmarksFolderAssetRenderer
	extends BaseJSPAssetRenderer<BookmarksFolder> implements TrashRenderer {

	public static final String TYPE = "bookmarks_folder";

	public BookmarksFolderAssetRenderer(BookmarksFolder folder) {
		_folder = folder;
	}

	@Override
	public BookmarksFolder getAssetObject() {
		return _folder;
	}

	@Override
	public String getClassName() {
		return BookmarksFolder.class.getName();
	}

	@Override
	public long getClassPK() {
		return _folder.getFolderId();
	}

	@Override
	public Date getDisplayDate() {
		return _folder.getModifiedDate();
	}

	@Override
	public long getGroupId() {
		return _folder.getGroupId();
	}

	@Override
	public String getIconCssClass() throws PortalException {
		if (BookmarksFolderServiceUtil.getFoldersAndEntriesCount(
				_folder.getGroupId(), _folder.getFolderId()) > 0) {

			return "icon-folder-open";
		}

		return super.getIconCssClass();
	}

	@Override
	public String getIconPath(ThemeDisplay themeDisplay) {
		try {
			if (BookmarksFolderServiceUtil.getFoldersAndEntriesCount(
					_folder.getGroupId(), _folder.getFolderId(),
					WorkflowConstants.STATUS_APPROVED) > 0) {

				return themeDisplay.getPathThemeImages() +
					"/common/folder_full_document.png";
			}
		}
		catch (Exception e) {
		}

		return themeDisplay.getPathThemeImages() + "/common/folder_empty.png";
	}

	@Override
	public String getJspPath(HttpServletRequest request, String template) {
		if (template.equals(TEMPLATE_FULL_CONTENT)) {
			return "/bookmarks/asset/folder_" + template + ".jsp";
		}
		else {
			return null;
		}
	}

	@Override
	public String getPortletId() {
		AssetRendererFactory<BookmarksFolder> assetRendererFactory =
			getAssetRendererFactory();

		return assetRendererFactory.getPortletId();
	}

	@Override
	public int getStatus() {
		return _folder.getStatus();
	}

	@Override
	public String getSummary(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		return _folder.getDescription();
	}

	@Override
	public String getThumbnailPath(PortletRequest portletRequest)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		int entriesCount = BookmarksEntryServiceUtil.getEntriesCount(
			_folder.getGroupId(), _folder.getFolderId());
		int foldersCount = BookmarksFolderServiceUtil.getFoldersCount(
			_folder.getGroupId(), _folder.getFolderId());

		if ((entriesCount > 0) || (foldersCount > 0)) {
			return themeDisplay.getPathThemeImages() +
				"/file_system/large/folder_full_bookmark.png";
		}

		return themeDisplay.getPathThemeImages() +
			"/file_system/large/folder_empty_bookmark.png";
	}

	@Override
	public String getTitle(Locale locale) {
		return TrashUtil.getOriginalTitle(_folder.getName());
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public PortletURL getURLEdit(
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws Exception {

		PortletURL portletURL = PortalUtil.getControlPanelPortletURL(
			liferayPortletRequest, BookmarksPortletKeys.BOOKMARKS_ADMIN, 0,
			PortletRequest.RENDER_PHASE);

		portletURL.setParameter(
			"mvcRenderCommandName", "/bookmarks/edit_folder");
		portletURL.setParameter(
			"folderId", String.valueOf(_folder.getFolderId()));

		return portletURL;
	}

	@Override
	public PortletURL getURLView(
			LiferayPortletResponse liferayPortletResponse,
			WindowState windowState)
		throws Exception {

		AssetRendererFactory<BookmarksFolder> assetRendererFactory =
			getAssetRendererFactory();

		PortletURL portletURL = assetRendererFactory.getURLView(
			liferayPortletResponse, windowState);

		portletURL.setParameter("mvcRenderCommandName", "/bookmarks/view");
		portletURL.setParameter(
			"folderId", String.valueOf(_folder.getFolderId()));
		portletURL.setWindowState(windowState);

		return portletURL;
	}

	@Override
	public String getURLViewInContext(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse,
		String noSuchEntryRedirect) {

		return getURLViewInContext(
			liferayPortletRequest, noSuchEntryRedirect,
			"/bookmarks/find_folder", "folderId", _folder.getFolderId());
	}

	@Override
	public long getUserId() {
		return _folder.getUserId();
	}

	@Override
	public String getUserName() {
		return _folder.getUserName();
	}

	@Override
	public String getUuid() {
		return _folder.getUuid();
	}

	@Override
	public boolean hasEditPermission(PermissionChecker permissionChecker)
		throws PortalException {

		return BookmarksFolderPermissionChecker.contains(
			permissionChecker, _folder, ActionKeys.UPDATE);
	}

	@Override
	public boolean hasViewPermission(PermissionChecker permissionChecker)
		throws PortalException {

		return BookmarksFolderPermissionChecker.contains(
			permissionChecker, _folder, ActionKeys.VIEW);
	}

	@Override
	public boolean include(
			HttpServletRequest request, HttpServletResponse response,
			String template)
		throws Exception {

		request.setAttribute(BookmarksWebKeys.BOOKMARKS_FOLDER, _folder);

		return super.include(request, response, template);
	}

	private final BookmarksFolder _folder;

}