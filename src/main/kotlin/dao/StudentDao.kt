package dao

import domain.Student
import java.sql.*
import java.util.Properties

interface StudentDao {
    fun addStudent(student: Student)
    fun getStudents() : List<Student>
    fun deleteStudents(studentNumber: String) : Int
    fun deleteAll()
}

class MySqlStudentDao(jdbcUrl: String, username: String, password: String) : StudentDao {
    private val GET_STUDENT_QUERY = "SELECT sid, name, student_number, test FROM students"
    private val ADD_STUDENT_QUERY = "INSERT INTO students VALUES (?, ?, ?, ?)"
    private val DELETE_STUDENT_QUERY = "DELETE from students where student_number=?"
    private val TRUNCATE_QUERY = "TRUNCATE TABLE students"

    private val url: String
    private val connProp: Properties

    init {
        this.url = jdbcUrl
        connProp = Properties()
        connProp["user"] = username
        connProp["password"] = password
    }

    private fun getConnection() : Connection {
        return DriverManager.getConnection(url, connProp)
    }

    override fun addStudent(student: Student) {
        val stmt : PreparedStatement? = getConnection().prepareStatement(ADD_STUDENT_QUERY)
        stmt?.setInt(1, student.sid)
        stmt?.setString(2, student.name)
        stmt?.setString(3, student.studentNumber)
        stmt?.setInt(4, student.test)
        stmt?.executeUpdate()
    }

    override fun deleteStudents(studentNumber: String): Int {
        val stmt : PreparedStatement = getConnection().prepareStatement(DELETE_STUDENT_QUERY)
        stmt.setString(1, studentNumber)
        return stmt.executeUpdate()
    }

    override fun getStudents(): List<Student> {
        val stmt: PreparedStatement = getConnection().prepareStatement(GET_STUDENT_QUERY)
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
            return listOf()
        }
    }

    override fun deleteAll() {
        val stmt: PreparedStatement = getConnection().prepareStatement(TRUNCATE_QUERY)
        stmt.executeUpdate()
    }
}