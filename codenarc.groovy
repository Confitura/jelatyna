ruleset {
    ruleset('rulesets/basic.xml')
    ruleset('rulesets/braces.xml')
    ruleset('rulesets/concurrency.xml')
    ruleset('rulesets/convention.xml') {
        'NoDef' doNotApplyToFilesMatching: '.*Spec.groovy'
    }
    ruleset('rulesets/design.xml') {
        'PrivateFieldCouldBeFinal' enabled: false
    }
    ruleset('rulesets/dry.xml') {
        'DuplicateStringLiteral' enabled: false
        'DuplicateMapLiteral' enabled: false
        'DuplicateNumberLiteral' enabled: false
        'DuplicateListLiteral' enabled: false
    }
    ruleset('rulesets/formatting.xml') {
        'ClassJavadoc' enabled: false
        'SpaceAroundMapEntryColon' enabled: false
        'SpaceAfterOpeningBrace' enabled: false
        'SpaceBeforeClosingBrace' enabled: false
    }
    ruleset('rulesets/generic.xml')
    ruleset('rulesets/groovyism.xml')
    ruleset('rulesets/imports.xml') {
        'MisorderedStaticImports' enabled: false
        'NoWildcardImports' enabled: false
    }
    ruleset('rulesets/jdbc.xml')
    ruleset('rulesets/junit.xml') {
        'JUnitPublicNonTestMethod' enabled: false
    }
    ruleset('rulesets/logging.xml')
    ruleset('rulesets/security.xml')
    ruleset('rulesets/size.xml') {
        'CrapMetric' enabled: false
    }
    ruleset('rulesets/unnecessary.xml') {
        'UnnecessaryReturnKeyword' enabled: false
        'UnnecessaryGString' enabled: false
        'UnnecessaryBooleanExpression' enabled: false
    }
    ruleset('rulesets/unused.xml')
}
