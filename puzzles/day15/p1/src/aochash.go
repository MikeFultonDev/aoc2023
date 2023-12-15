package main

import ( 
  "fmt"
  "strings"
  "log"
  "os"
)

func hashit(sequence string) int {
  bytes := []byte(sequence)
  hash := 0

  for i:=0; i<len(bytes); i++ {
    hash += int(bytes[i])
    hash *= 17
    hash = hash % 256
  }
  //fmt.Fprintf(os.Stdout, "%s hashes to %d\n", sequence, hash)
  return hash
}
  
func main() {
    var numArgs = len(os.Args)
    if (numArgs != 2) {
      fmt.Fprintf(os.Stderr, "Syntax: aochash <hash-file>\n");
      os.Exit(4)
    }
    content, err := os.ReadFile(os.Args[1])
    if err != nil {
        log.Fatal(err)
    }
    var rawData = string(content)
    var sequence = strings.Split(rawData, ",")
    var sum = 0
    var replacer = strings.NewReplacer("\n", "")
    for i:=0; i<len(sequence); i++ {
      sequence[i] = replacer.Replace(sequence[i])
      sum += hashit(sequence[i])
    }
    fmt.Fprintf(os.Stdout, "Sum of hashes: %d\n", sum)
}
