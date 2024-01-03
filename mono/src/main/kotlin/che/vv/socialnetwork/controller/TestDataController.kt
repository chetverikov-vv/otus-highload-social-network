package che.vv.socialnetwork.controller

import che.vv.socialnetwork.controller.model.request.RegisterRequest
import com.opencsv.CSVReader
import mu.KotlinLogging
import org.springframework.util.ResourceUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.io.FileReader

@RestController
class TestDataController(private val authController: AuthController) {
    private val logger = KotlinLogging.logger { }
    @GetMapping("/user/load-test-data")
    fun loadTestData() {
        val csvData = ResourceUtils.getFile("classpath:test-users.csv")
        val csvReader = CSVReader(FileReader(csvData))
        val requests = csvReader.readAll().toRegisterRequests()
        requests.forEachIndexed { index, registerRequest ->
            authController.register(registerRequest)
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