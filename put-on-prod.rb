require "fileutils"
system("mvn clean install")
FileUtils.cp_r("dist/.", "#{ENV["QIN_ROOT"]}/Prod")
