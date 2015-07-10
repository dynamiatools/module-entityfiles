/**
 * Dynamia Soluciones IT S.A.S Todos los Derechos Reservados (c) - 2015
 *
 * Prohibida la reproduccion parcial o total de este archivo de código fuente en proyectos de software NO realizados por Dynamia Soluciones IT S.A.S. Cualquier
 * otro archivo de código fuente o librería que haga referencia a este tendrá la misma licencia.
 *
 * mas info: http://www.dynamiasoluciones.com/licencia.html
 *
 * Desarrollado por: Ing. Mario A. Serrano Leones 
 * Email: mario@dynamiasoluciones.com
 */
package tools.dynamia.modules.entityfile.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import tools.dynamia.commons.StringUtils;
import tools.dynamia.domain.BaseEntity;
import tools.dynamia.domain.contraints.NotEmpty;
import tools.dynamia.integration.Containers;
import tools.dynamia.io.IOUtils;
import tools.dynamia.modules.entityfile.StoredEntityFile;
import tools.dynamia.modules.entityfile.domain.enums.EntityFileState;
import tools.dynamia.modules.entityfile.enums.EntityFileType;
import tools.dynamia.modules.entityfile.service.EntityFileService;
import tools.dynamia.modules.saas.api.AccountAware;

@Entity
@Table(name = "mod_entity_files")
public class EntityFile extends BaseEntity implements AccountAware {

	@OneToMany(mappedBy = "parent")
	private List<EntityFile> children;
	@NotNull(message = "Enter EntityFile targetEntity name")
	private String targetEntity;
	private Long targetEntityId;
	private String targetEntitySId;
	@NotNull(message = "Enter EntityFile name")
	private String name;
	@NotNull(message = "Enter EntityFile tyoe")
	private EntityFileType type;
	private String extension = "dir";
	private String contentType;
	@ManyToOne
	private EntityFile parent;
	private boolean shared;
	@Column(name = "fileSize")
	private Long size = 0l;
	@Column(length = 1000)
	private String description;
	@Column(name = "fileState")
	@Enumerated(EnumType.ORDINAL)
	private EntityFileState state;
	@NotEmpty
	private String uuid = StringUtils.randomString();
	@Column(length = 1000)
	private String storageInfo;
	private String subfolder;

	private Long accountId;

	public String getTargetEntitySId() {
		return targetEntitySId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getStorageInfo() {
		return storageInfo;
	}

	public void setStorageInfo(String storageInfo) {
		this.storageInfo = storageInfo;
	}

	public void setTargetEntitySId(String targetEntitySId) {
		this.targetEntitySId = targetEntitySId;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getTargetEntity() {
		return targetEntity;
	}

	public void setTargetEntity(String targetEntity) {
		this.targetEntity = targetEntity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String descripcion) {
		this.description = descripcion;
	}

	public EntityFile getParent() {
		return parent;
	}

	public void setParent(EntityFile parentDirectory) {
		if (parentDirectory != null && parentDirectory.getType() == EntityFileType.DIRECTORY) {
			this.parent = parentDirectory;
		}
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String ext) {
		this.extension = ext;
	}

	public List<EntityFile> getChildren() {
		return children;
	}

	public void setChildren(List<EntityFile> filesHijos) {
		this.children = filesHijos;
	}

	public Long getTargetEntityId() {
		return targetEntityId;
	}

	public void setTargetEntityId(Long idDependencia) {
		this.targetEntityId = idDependencia;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isShared() {
		return shared;
	}

	public void setShared(boolean shared) {
		this.shared = shared;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long sizeFile) {
		this.size = sizeFile;
	}

	public EntityFileType getType() {
		return type;
	}

	public void setType(EntityFileType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getSizeAsString() {
		if (size != null) {
			return IOUtils.formatFileSize(size.longValue());
		} else {
			return null;
		}
	}

	public void setState(EntityFileState state) {
		this.state = state;
	}

	public EntityFileState getState() {
		return state;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getSubfolder() {
		return subfolder;
	}

	public void setSubfolder(String subfolder) {
		this.subfolder = subfolder;
	}

	/**
	 * Download this EntityFile. Internally its use
	 * EntityFileService.download(this);
	 * 
	 * @return StoredEntityFile
	 */
	public StoredEntityFile getStoredEntityFile() {
		EntityFileService service = Containers.get().findObject(EntityFileService.class);
		if (service == null) {
			throw new NullPointerException("No EntityService was found to download Entity File");
		}
		return service.download(this);
	}

}
