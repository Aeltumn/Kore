plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
	`maven-publish`	// `publish-conventions` // Aeltumn - change plugin
}

metadata {
	name = "Kore"
	description = "A Kotlin DSL to create Minecraft datapacks."
}

repositories {
	mavenCentral()
	maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
	implementation(libs.kotlinx.serialization)
	implementation(kotlin("reflect"))
	api(libs.knbt)
	api(libs.joml)

	testImplementation(libs.kotlin.dotenv)
}

kotlin {
	jvmToolchain(17)

	compilerOptions {
		freeCompilerArgs = listOf("-Xcontext-receivers")
	}
}

tasks.compileKotlin {
	val generatedFolder = file("src/main/kotlin/io/github/ayfri/kore/generated")
	if (!generatedFolder.exists()) {
		dependsOn(":generation:run")
	}
}

var runUnitTests = tasks.register<JavaExec>("runUnitTests") {
	description = "Runs the unit tests."
	group = "verification"

	classpath = sourceSets.test.get().runtimeClasspath
	mainClass = "${Project.GROUP}.MainKt"
	shouldRunAfter("test")
}

tasks.test {
	dependsOn(runUnitTests)
}

// Aeltumn start - add publishing
group = "io.github.ayfri.kore"
version = "local"

afterEvaluate {
	val sourceSets = extensions.getByName("sourceSets") as SourceSetContainer
	val sourcesJar by tasks.creating(org.gradle.api.tasks.bundling.Jar::class) {
		from(sourceSets.named<SourceSet>("main").get().allSource)
		archiveClassifier.set("sources")
	}

	extensions.configure<PublishingExtension> {
		publications {
			create<MavenPublication>("maven") {
				from(components["kotlin"])
				artifact(sourcesJar)
				artifactId = "kore"
			}
		}
	}
}
// Aeltum end - add publishing