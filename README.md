# Cuke-Report-Converter

**Cuke-Report-Converter** is a library that parses cucumber json report files and maps the report to java objects. It also provides a conversion method that maps those objects to a new structure ( cuke model ).

### Models
The library defines two models.

#### Cucumber Json Report Model
Under package `io.github.alexopa.cukereportconverter.model.jsonreport` the classes that model that json cucumber report under defined. Those classes are prefixed with `Cuke...`, ie:
```
io.github.alexopa.cukereportconverter.model.jsonreport.Feature
io.github.alexopa.cukereportconverter.model.jsonreport.Tag
io.github.alexopa.cukereportconverter.model.jsonreport.TagLocation
io.github.alexopa.cukereportconverter.model.jsonreport.Element
io.github.alexopa.cukereportconverter.model.jsonreport.Step
io.github.alexopa.cukereportconverter.model.jsonreport.StepMatch
io.github.alexopa.cukereportconverter.model.jsonreport.StepResult
io.github.alexopa.cukereportconverter.model.jsonreport.Embedding
io.github.alexopa.cukereportconverter.model.jsonreport.Row
io.github.alexopa.cukereportconverter.model.jsonreport.DocString
io.github.alexopa.cukereportconverter.model.jsonreport.MatchArgument
io.github.alexopa.cukereportconverter.model.jsonreport.MatchArgument
```

#### Cuke Model
The library defines a new model, which will be mentioned as **[cuke model]** from now on, This model is defined under package `io.github.alexopa.cukereporconverter.model.cuke`. Purpose of this model is to represent the cucumber objects of the json reports in a more readable/user-friendly way. The following objects are available:
```
io.github.alexopa.cukereportconverter.model.cuke.CukeTestRun
io.github.alexopa.cukereportconverter.model.cuke.CukeFeature
io.github.alexopa.cukereportconverter.model.cuke.CukeScenario
io.github.alexopa.cukereportconverter.model.cuke.CukeScenarioResult
io.github.alexopa.cukereportconverter.model.cuke.CukeScenarioType
io.github.alexopa.cukereportconverter.model.cuke.CukeStep
io.github.alexopa.cukereportconverter.model.cuke.CukeStepMatch
io.github.alexopa.cukereportconverter.model.cuke.CukeStepResult
io.github.alexopa.cukereportconverter.model.cuke.CukeEmbedding
io.github.alexopa.cukereportconverter.model.cuke.CukeMatchArgument
```

***Note:*** `CukeTestRun` is a wrapper object for the list of features available.

For examples, a `CukeScenario` objects contains the following elements ( and a few more):
```
private List<CukeStep> beforeSteps;
private List<CukeStep> afterSteps;
private List<CukeStep> backgroundSteps;
private List<CukeStep> scenarioSteps;
```
This structure simplifies the handling of cucumber objects while generating a report or using those items for any reason.
Some of the cucumber model classes provide also some other extra properties that have been calculated while converting the cucumber json model.
For example, in the `CukeScenario` class, the following are available and calculated:
```
private long beforeStepsDuration;
private long afterStepsDuration;
private long backgroundStepsDuration;
private long scenarioStepsDuration;
private long totalDuration;
private ScenarioResult result;
```


## Usage
### Parsing cucumber report to java objects
```
CukeConverter  cukeConverter = new  CukeConverter();
File  jsonReportFile1;

List<Feature> features = cukeConverter.convertCucumberJsonFiles(Arrays.asList(jsonReportFile1));
```
The convert method can also parse multiple json files at the same time and combine them in the same list of `Feature` objects.
```
File  jsonReportFile1;
File  jsonReportFile2;

List<Feature> features = cukeConverter.convertCucumberJsonFiles(Arrays.asList(jsonReportFile1, jsonReportFile2));
```


### Converting json report classes to cuke model
The converter service provides a method that converts the above classes to the cuke model:
```
List<Feature> jsonFeatures = ...;
List<CukeFeature> cukeFeatures = cukeConverter.convertToCukeFeature(jsonFeatures);
```

There is also one more method available that converts directly the json files to the cuke model:
```
File  jsonReportFile1;
File  jsonReportFile2;

CukeTestRun  testRun = cukeConverter.convertToTestRun(Arrays.asList(jsonReportFile1, jsonReportFile2));
```

## Configuration
The following parameters are available:
| Property Name | Default Value  | Description |
|--|--|--|
| cuke-converter.failOnError  | false | When property is set to `true`, then if there is a failure while parsing the cucumber json files, an exception is thrown. Otherwise, a warning message is logged and the processing continues with the rest if the json files, if any.  |
| cuke-converter.mergeFeatures | true | When property is set to `true`, then if there are features with the same name, their scenarios are merged under the same `CukeFeature` object. Otherwise, a separate `CukeFeature` object is created |


The properties can be configured in the following ways:
- In `cuke-report-converter.properties` file that should be placed under `resources` folder.
- As a java parameter, ie: `-Dcuke-converter.failOnError=true`
