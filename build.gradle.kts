plugins {
    id("fabric-loom") version "1.0-SNAPSHOT"
    id("com.modrinth.minotaur") version "2.+"
}

version = "1.0.0"
group = "shateq.fabric"
description = "Ban entities from rendering"
base.archivesName.set("no-peeking-$version-mc${properties["mc"]}")

repositories {
    maven("https://maven.wispforest.io")
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://jitpack.io")
}

dependencies {
    minecraft("com.mojang:minecraft:${properties["mc"]}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${properties["loader"]}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${properties["fapi"]}")

    modApi("com.terraformersmc:modmenu:${properties["modmenu"]}") //from linkie

    modImplementation("io.wispforest:owo-lib:${properties["owo_version"]}")
    annotationProcessor("io.wispforest:owo-lib:${properties["owo_version"]}")
    include("io.wispforest:owo-sentinel:${properties["owo_version"]}")

    modRuntimeOnly("com.github.astei:lazydfu:0.1.2") //from Jitpack
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))
loom.mixin.defaultRefmapName.set("nopeeking.refmap.json")

tasks {
    jar {
        from("LICENSE") {
            rename { "${it}_$archiveBaseName" }
        }
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    processResources {//fabric.mod.json
        filteringCharset = Charsets.UTF_8.name()
        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }
}

modrinth {
    token.set("System.getenv(\"MODRINTH_TOKEN\")") // This is the default. Remember to have the MODRINTH_TOKEN environment variable set or else this will fail, or set it to whatever you want - just make sure it stays private!
    projectId.set("no-peeking")

    versionType.set("beta")
    versionNumber.set(version.toString())
    versionName.set("$version for Minecraftt ${properties["mc"]}")

    uploadFile.set(tasks.remapJar.name) // With Loom, this MUST be set to `remapJar` instead of `jar`!
    gameVersions.addAll("1.18", "1.18.1", "1.18.2") // Must be an array, even with only one version
    dependencies {
        // The scope can be `required`, `optional`, `incompatible`, or `embedded`
        required.project("fabric-api")
    }
}
