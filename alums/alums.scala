import scala.io.Source
import java.io.FileWriter
import scala.util.matching.Regex

val lines = Source.fromFile("alums on website.txt").getLines.toList
val alums = lines.tail
val len = alums.length

val nameline = new Regex(""".*, PhD \([0-9]*\)""")
val delregex = new Regex("""Chair|Currently""")
val diss = new Regex("""Dissertation""")
var delete = 0
val newlines = (0 to (len - 1)).map(i => {                
  if (delete == 0) {                                        
     val line = alums(i)                                     
     if (nameline.findAllIn(line).length > 0) line         
     else if (delregex.findAllIn(line).length > 0) delete = 1
     else if (diss.findAllIn(line).length > 0) {}
     else line + "\n"
   } else delete = 0
}).filter(x => x.toString.length > 2).mkString("\n")

/*
val out = new FileWriter("alums.txt")
out.write(newlines.mkString("\n"))
out.close
*/

val alumni = newlines.split("\n\n").map(x => x.split("\n").toList)
val page = Source.fromURL("http://ling.umd.edu/people/alums/").mkString
alumni.foreach(alum => {
  val name = alum(0).split(",")(0)
  val siteregex = new Regex(name + """.*\n"""*8 + """.*href="(.*)">""")
  //</p>\n *\n.*\n.*\n.*\n.*Dissertation.*\n.*\n.*\n.*<a href="(.*)">""")
  val pattern = siteregex.findFirstIn(page)
  //if (pattern.length == 0) println(name)
  //print(pattern.length)
  val siteregex(site) = pattern.get
  if (site == "") println(name + "--EMPTY")
  else {
    val dispage = Source.fromURL(site).mkString
    val disnameregex = new Regex(alum(1))
    val matches = disnameregex.findAllIn(dispage)
    if (matches.length == 0) {
      val string = alum(1)
      val anycase = string.toList.map(ch => {
        if (ch == '?') "." else "("+ch.toLowerCase+"|"+ch.toUpperCase+")"
      }).mkString
      val regexInAnyCase = new Regex(anycase)
      val lowermatches = regexInAnyCase.findAllIn(dispage)
      if (lowermatches.length == 0) println(name)
    }
  }
})


