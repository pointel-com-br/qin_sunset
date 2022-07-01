require "fileutils"
system("mvn clean install")
FileUtils.cp_r("dist/.", "../../Test")
