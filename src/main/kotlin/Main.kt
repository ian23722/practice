import dao.MySqlStudentDao
import domain.Student

val username = "app"
val password = "1234"
val jdbcUrl = "jdbc:mysql://144.24.86.115:3306/beta"

fun main() {
    println("DB Connection test")

    val studentDao = MySqlStudentDao(jdbcUrl, username, password)

    println("clean up table")
    studentDao.deleteAll()

    val student1 = Student(2, "김서현", "124", 4)
    val student2 = Student(3, "이가현", "125", 4)
    val student3 = Student(4, "김나형", "126", 4)

    println("students added")
    studentDao.addStudent(student1)
    studentDao.addStudent(student2)
    studentDao.addStudent(student3)

    studentDao.getStudents().forEach { student -> println(student) }

    val student4 = Student(5, "김용철", "130", 4)

    println("wrong student added")
    studentDao.addStudent(student4)

    studentDao.getStudents().forEach { student -> println(student) }

    println("wrong student deleted")
    studentDao.deleteStudents(student4.studentNumber)
    studentDao.getStudents().forEach { student -> println(student) }

    // This will delete all records in the student table comment out if you want to keep the records
    println("clean up table")
    studentDao.deleteAll()
}
