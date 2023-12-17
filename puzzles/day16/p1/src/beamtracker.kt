fun readFile(fileName: String): List<String> 
  = File(fileName).readLines()

fun main(args: Array<String) {
  if (args.length != 1) {
    print("Syntax: beamtracker <beam file>")
    return
  }

  var lines = readFile(args[0])

  print("Read " + lines.length + " lines")
}
  
