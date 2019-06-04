# EntifyFiles for DynamiaTools

This extension allow attach files to database entities. Files are saved to locale disk (or aws s3) and metadata are store in database.

## Modules
- Core: Domain and API
- UI: Actions and views for user interface integration.
- S3 for Amazon Simple Storage Service

## Installation

**Maven**
```xml
<dependency>
  <groupId>tools.dynamia.modules</groupId>
  <artifactId>tools.dynamia.modules.entityfiles</artifactId>
  <version>6.2.1.Final</version>
</dependency>

<dependency>
  <groupId>tools.dynamia.modules</groupId>
  <artifactId>tools.dynamia.modules.entityfiles.ui</artifactId>
  <version>6.2.1.Final</version>
</dependency>

```

**Gradle**
```groovy
compile 'tools.dynamia.modules:tools.dynamia.modules.entityfiles:6.2.1'
compile 'tools.dynamia.modules:tools.dynamia.modules.entityfiles.ui:6.2.1'
```

