package tools.dynamia.modules.entityfile.local;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import tools.dynamia.integration.Containers;
import tools.dynamia.io.IOUtils;
import tools.dynamia.io.ImageUtil;
import tools.dynamia.io.impl.SpringResource;
import tools.dynamia.modules.entityfile.StoredEntityFile;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.modules.entityfile.enums.EntityFileType;
import tools.dynamia.modules.entityfile.service.EntityFileService;
import tools.dynamia.modules.saas.api.AccountServiceAPI;

public class LocalEntityFileStorageHandler extends ResourceHttpRequestHandler {

	private LocalEntityFileStorage storage;
	private EntityFileService service;
	private AccountServiceAPI accountServiceAPI;

	@Override
	protected Resource getResource(HttpServletRequest request) {
		if (service == null) {
			service = Containers.get().findObject(EntityFileService.class);
		}
		if (storage == null) {
			storage = Containers.get().findObject(LocalEntityFileStorage.class);
		}

		if (accountServiceAPI == null) {
			accountServiceAPI = Containers.get().findObject(AccountServiceAPI.class);
		}

		File file = null;
		String uuid = getParam(request, "uuid", null);
		if (uuid == null) {
			return null;
		}

		Long currentAccountId = accountServiceAPI.getCurrentAccountId();
		EntityFile entityFile = service.getEntityFile(uuid);

		if (entityFile != null && (entityFile.isShared() || entityFile.getAccountId().equals(currentAccountId))) {

			StoredEntityFile storedEntityFile = entityFile.getStoredEntityFile();

			file = storedEntityFile.getRealFile();

			if (entityFile.getType() == EntityFileType.IMAGE) {

				if (isThumbnail(request)) {
					file = createOrLoadThumbnail(file, entityFile, request);
				}

				if (!file.exists()) {
					SpringResource notFoundResource = (SpringResource) IOUtils.getResource("classpath:/web/tools/images/no-photo.jpg");
					return notFoundResource.getInternalResource();
				}
			}
		}
		if (file != null) {
			return new FileSystemResource(file);
		} else {
			return null;
		}
	}

	private boolean isThumbnail(HttpServletRequest request) {
		return getParam(request, "w", null) != null && getParam(request, "h", null) != null;
	}

	private File createOrLoadThumbnail(File realImg, EntityFile entityFile, HttpServletRequest request) {

		String w = getParam(request, "w", "200");
		String h = getParam(request, "h", "200");
		String subfolder = w + "x" + h;

		File realThumbImg = new File(realImg.getParentFile(), subfolder + "/" + realImg.getName());
		if (!realThumbImg.exists()) {
			if (realImg.exists()) {
				ImageUtil.resizeImage(realImg, realThumbImg, entityFile.getExtension(), Integer.parseInt(w), Integer.parseInt(h));
			}
		}
		return realThumbImg;

	}

	public String getParam(HttpServletRequest request, String name, String defaultValue) {
		String value = request.getParameter(name);
		if (value == null || value.trim().isEmpty()) {
			value = defaultValue;
		}
		return value;
	}
}