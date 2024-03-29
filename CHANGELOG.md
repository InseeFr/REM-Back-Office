# REM-Back-Office Change Log

## 1.0.0

### Features

#### Load a new partition (partitionEntity) of survey units for a household survey
- Create a new partition from a label
- Import new survey units from a csv file and add them to the new partition
- Import new survey units from a json and add them to the new partition

#### Manage a partition of survey units
- Add an existing survey unit to another partition
- Add a list of survey units to another partition
- Remove a survey unit from a partition (survey unit is not deleted)

#### Extract Data (Partitions and Survey Units)
- Get all survey units from a partition
- Get one survey unit
- Get all survey units identifiers from a partition
- Get a mapping table of survey units identifiers and the data source identifiers from a partition (csv output)
- Get one partition (get label from its identifier) 
- Get all partitions

#### Clean Data
- Delete a partition (links withs survey units are deleted but not the survey units)
- Delete a survey unit (links withs partitions are deleted too)

## 1.1.0

### Features
- Update Data of a survey unit

### Changes
- distinction between household members (main, surveyed, co-declarant)
- security configuration changed with spring-security 6.1.0: use lambda expression

### Refactoring
- test: pattern "Given When Then" without Mockito

### Dependencies updates
- spring-boot: 3.0.0 -> 3.1.0
- springdoc-openapi-stater-webmvc-ui: 2.0.0 -> 2.1.0
- opencsv: 5.7.0 -> 5.7.1
- pitest-maven: 1.11.0 -> 1.14.1
- pitest-junit5-plugin: 1.1.2 -> 1.2.0
- mapstruct-processor: 1.5.3.Final -> 1.5.5.Final
- lombok: 1.18.26 -> 1.18.28
- slf4j-api: 2.0.6 -> 2.0.7

## 1.2.0

### Refactoring
- Replaced the name "Sample" with "Partition" (many files changes)
- Created ApiError class to manage errors feedback
- Used Tags for groups of endpoints
- Replace name of TypeUnit enum to Context

## 1.3.0

### Features
- Add list of ids into import returns
- Add "externals" field into survey unit: save in database without modification
- Add number of SurveyUnit into partition endpoint
- Import census survey units
- Empty a partition

### Refactoring
- use JsonView to manage the fields to return
- add content description in the OpenAPI documentation
- delete Serializable from Entity classes

### Dependencies updates
- spring-boot: 3.1.0 -> 3.1.1
- pitest-maven: 1.14.1 -> 1.14.4

## 1.3.1

### Dependencies updates
- spring-boot: 3.1.1 -> 3.2.4
- lombok: 1.18.28 -> 1.18.30
- slf4j-api: 2.0.7 -> 2.0.11
- springdoc-openapi-stater-webmvc-ui: 2.1.0 -> 2.3.0
- opencsv: 5.7.1 -> 5.9
- pitest-maven: 1.14.4 -> 1.15.6
- pitest-junit5-plugin: 1.2.0 -> 1.2.1
- jacoco-maven-plugin: 0.8.10 -> 0.8.11