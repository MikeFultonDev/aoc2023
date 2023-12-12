
String file = args[0]

new File(file).eachLine { line ->
    println line
}

