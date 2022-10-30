import org.kohsuke.github.*
import java.io.BufferedReader

class GitHubEntity(val repositoryName : String) {

    val contentList : MutableList<GHContent>
    val rootRepository : GHRepository
    val github : GitHub
    init {
        github  = GitHubBuilder.fromEnvironment().build()
        rootRepository = github.getRepository(repositoryName)
        contentList = rootRepository.getDirectoryContent("")
        print("Here")
    }

    fun getFiles() : List<String> {
        val filesList : MutableList<GHContent> = ArrayList()
        for (content in contentList) {
            filesList.addAll(getContent(content))
        }
        return filesList.map { element -> element.read().bufferedReader().use(BufferedReader::readText) }
    }

    private fun getContent(content : GHContent) : MutableList<GHContent> {
        val filesList : MutableList<GHContent> = ArrayList()
        val phpRegex: Regex = Regex("[.a-zA-Z_-]*.php")
        if (content.isDirectory) {
            for (nextContent in rootRepository.getDirectoryContent(content.path)) {
                filesList.addAll(getContent(nextContent))
            }
        }
        if (content.isFile && phpRegex.matches(content.name)) {
            filesList.add(content)
            println(content.name)
        }
        return filesList
    }
}