val publicationName = "chess-rules"

plugins {
    kotlin("jvm") version "1.3.72"
    `maven-publish`
    id("com.jfrog.bintray") version "1.8.5"
}

group = "com.ne4ephoji"
version = "0.0.2"

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("junit:junit:4.13")
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

publishing {
    publications {
        create<MavenPublication>(publicationName) {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["kotlin"])
            artifact(sourcesJar)
        }
    }
}

bintray {
    val items = HashMap<String, String>()
    File("local.properties").forEachLine {
        items[it.split("=")[0]] = it.split("=")[1]
    }
    user = items["bintray.user"]!!
    key = items["bintray.apiKey"]!!
    dryRun = false
    publish = true
    setPublications(publicationName)

    pkg(delegateClosureOf<com.jfrog.bintray.gradle.BintrayExtension.PackageConfig> {
        repo = publicationName
        name = project.name
        desc = "Simple library to use chess rules written on Kotlin"
        websiteUrl = "https://github.com/ne4ephoji/${project.name}"
        issueTrackerUrl = "https://github.com/ne4ephoji/${project.name}/issues"
        vcsUrl = "https://github.com/ne4ephoji/${project.name}.git"
        setLicenses("GPL-3.0")
        setLabels("kotlin", "chess")
        publicDownloadNumbers = true
    })
}