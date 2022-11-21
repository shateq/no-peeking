plugins {
    id("fabric-loom") version "0.12-SNAPSHOT"
    id("com.modrinth.minotaur") version "2.+"
}

version = "1.0.0"
group = "shateq.fabric"
description = "Ban entities from rendering"
base.archivesName.set("no-peeking-$version-mc"+properties["mc"] as String)

repositories {
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://jitpack.io")
}

dependencies {
    minecraft("com.mojang", "minecraft", properties["mc"] as String)
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc", "fabric-loader", properties["loader"] as String)
    modImplementation("net.fabricmc.fabric-api", "fabric-api", properties["fapi"] as String)

    implementation("com.google.code.gson", "gson", properties["gson"] as String)
    include("com.google.code.gson", "gson", properties["gson"] as String)

    modApi("com.terraformersmc", "modmenu", properties["modmenu"] as String)
    modApi("me.shedaniel.cloth", "cloth-config-fabric", properties["cloth"] as String) {
        exclude("net.fabricmc.fabric-api")
    }

    modRuntimeOnly("com.github.astei:lazydfu:0.1.2") //from Jitpack
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

loom {
    mixin.defaultRefmapName.set("nopeeking.refmap.json")
}

tasks {
    jar {
        from("LICENSE") {
            rename { "${it}_$archiveBaseName" }
        }
    }
    withType<JavaCompile> {
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
//	syncBodyFrom = rootProject.file("MOD.md").readText(Charsets.UTF_8)
    token.set("System.getenv(\"MODRINTH_TOKEN\")") // This is the default. Remember to have the MODRINTH_TOKEN environment variable set or else this will fail, or set it to whatever you want - just make sure it stays private!
    projectId.set("my-project") // This can be the project ID or the slug. Either will work!

    versionType.set("release") // This is the default -- can also be `beta` or `alpha`
    versionNumber.set(version.toString())
    versionName.set("1.0.0 for 1.18.2")

    uploadFile.set(tasks["remapJar"]) // With Loom, this MUST be set to `remapJar` instead of `jar`!
    gameVersions.addAll("1.18", "1.18.1", "1.18.2") // Must be an array, even with only one version
    loaders.add("fabric") // Must also be an array - no need to specify this if you're using Loom or ForgeGradle
    dependencies {
        // scope.type
        // The scope can be `required`, `optional`, `incompatible`, or `embedded`
        // The type can either be `project` or `version`
        required.project("fabric-api") // Creates a new required dependency on Fabric API
    }
}
