package che.vv.socialnetwork.presentation

import che.vv.socialnetwork.presentation.model.request.RegisterRequest
import com.opencsv.CSVReader
import mu.KotlinLogging
import org.springframework.util.ResourceUtils
import org.springframework.web.bind.annotation.GetMapping
import java.io.FileReader

class TestDataController(private val userController: UserController) {
    private val logger = KotlinLogging.logger { }

    @GetMapping("/load-test-data")
    fun loadTestData() {
        val csvData = ResourceUtils.getFile("classpath:test-users.csv")
        val csvReader = CSVReader(FileReader(csvData))
        val requests = csvReader.readAll().toRegisterRequests()
        requests.forEachIndexed { index, registerRequest ->
            userController.register(registerRequest)
            logger.info { "Saved $index value" }
        }
    }

    private fun List<Array<String>>.toRegisterRequests(): List<RegisterRequest> = this.map {
        RegisterRequest(
            firstName = it[1],
            secondName = it[0],
            birthDate = it[2],
            biography = it[3],
            city = it[4],
            password = "mypass"
        )
    }
}