# REM-Back-Office Change Log

## 1.0.0

### Features

#### Load a new partition (sample) of survey units for a household survey
- Create a new partition from a label
- Import new survey units from a csv file and add them to the new partition
- Import new survey units from a json and add them to the new partition

#### Manage a partition of survey units
- Add existing a survey unit to another partition
- Add existing a list of survey units to another partition
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