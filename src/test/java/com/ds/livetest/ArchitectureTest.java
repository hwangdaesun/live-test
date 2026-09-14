package com.ds.livetest;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.ds.livetest")
class ArchitectureTest {

  @ArchTest
  static final ArchRule domainMustNotDependOnOuterLayers =
      noClasses()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage("..presentation..", "..service..", "..repository..")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule repositoryMustNotDependOnUpperLayers =
      noClasses()
          .that()
          .resideInAPackage("..repository..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage("..presentation..", "..service..")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule serviceMustNotDependOnPresentation =
      noClasses()
          .that()
          .resideInAPackage("..service..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..presentation..")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule presentationMustNotDependOnRepository =
      noClasses()
          .that()
          .resideInAPackage("..presentation..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..repository..")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule externalSupportMustNotDependOnDomainLayers =
      noClasses()
          .that()
          .resideInAPackage("..support.external..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage("..presentation..", "..service..", "..repository..", "..domain..")
          .allowEmptyShould(true);
}
