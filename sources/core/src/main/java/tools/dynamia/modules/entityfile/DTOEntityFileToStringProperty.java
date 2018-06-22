package tools.dynamia.modules.entityfile;

import tools.dynamia.commons.BeanUtils;
import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;
import tools.dynamia.commons.reflect.PropertyInfo;
import tools.dynamia.domain.DataTransferObjectPropertyProvider;
import tools.dynamia.integration.sterotypes.Provider;
import tools.dynamia.modules.entityfile.domain.EntityFile;

@Provider
public class DTOEntityFileToStringProperty implements DataTransferObjectPropertyProvider {

    private LoggingService logger = new SLF4JLoggingService(DTOEntityFileToStringProperty.class);

    @Override
    public boolean transferPropertyValue(Object dto, Object propertyValue, PropertyInfo propertyInfo) {
        if (propertyValue instanceof EntityFile) {
            EntityFile entityFile = (EntityFile) propertyValue;
            PropertyInfo dtoProperty = BeanUtils.getPropertyInfo(dto.getClass(), propertyInfo.getName());
            if (dtoProperty != null && dtoProperty.is(String.class)) {
                try {
                    String url = entityFile.getStoredEntityFile().getUrl();
                    BeanUtils.setFieldValue(dtoProperty, dto, url);
                    return true;
                } catch (Exception e) {
                    logger.error("Cannot transfer property value " + propertyValue + " to DTO  " + dto);
                }
            }
        }

        return false;
    }
}
