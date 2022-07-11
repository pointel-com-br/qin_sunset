require "fileutils"
system("mvn clean install")
FileUtils.cp_r("dist/.", "#{ENV["QIN_ROOT"]}/Prod")
FileUtils.cp("start.bat", "#{ENV["QIN_ROOT"]}/Prod")
FileUtils.cp("start.sh", "#{ENV["QIN_ROOT"]}/Prod")
