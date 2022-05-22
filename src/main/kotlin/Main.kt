import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.Properties

fun main(args: Array<String>) {
    println("DB Connection test")

    val app = App()
    val student = Student(1, "ian", "123", 4)
//    app.addStudent(student)
    app.printStudents()
}

class App() {
    private val connection : Connection

    init {
        connection = connectDB() ?: throw RuntimeException("Could not connect the DB")
        println(connection)
    }

    fun addStudent(student: Student) {
        val stmt : PreparedStatement? = connection?.prepareStatement("INSERT INTO students VALUES (?, ?, ?, ?)");
        stmt?.setInt(1, student.sid);
        stmt?.setString(2, student.name);
        stmt?.setString(3, student.studentNumber);
        stmt?.setInt(4, student.test);
        stmt?.executeUpdate();
    }

    fun printStudents() {
        val students : List<Student> = getStudents() ?: return
        students.forEach { student -> println(student) }

    }

    private fun getStudents() : List<Student>? {
        val stmt : PreparedStatement = connection?.prepareStatement("SELECT sid, name, student_number, test FROM students")
            ?: return null
        try {
            val studentList = mutableListOf<Student>()
            val results : ResultSet = stmt.executeQuery()
            while (results.next()) {
                val sid = results.getInt("sid")
                val name = results.getString("name")
                val studentNumber = results.getString("student_number")
                val test = results.getInt("test")
                val student = Student(sid, name, studentNumber, test)
                studentList.add(student)
            }
            return studentList
        } catch (e: SQLException) {
            e.printStackTrace()
            return null
        }
    }

    private fun connectDB() : Connection? {
        val props = Properties()
        props.put("user", "app")
        props.put("password", "1234")
        val conn : Connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/beta", props)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return conn
    }
}

data class Student(val sid: Int, val name: String, val studentNumber: String, val test: Int);
