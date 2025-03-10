import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun suma(num1: Int, num2: Int, num3: Int): Int{
    return num1 + num2 + num3
}

fun datos(nom: String): String{
    return nom
}

fun fechacalculo(fechaIngresada: String): String {
    val fechaIng = LocalDate.parse(fechaIngresada)
    val fechaAct = LocalDate.now()

    val dias = ChronoUnit.DAYS.between(fechaIng, fechaAct)

    val meses = ChronoUnit.MONTHS.between(fechaIng, fechaAct) % 12
    val semanas = dias / 7
    val horas = ChronoUnit.HOURS.between(fechaIng.atStartOfDay(), fechaAct.atStartOfDay())
    val minutos = ChronoUnit.MINUTES.between(fechaIng.atStartOfDay(), fechaAct.atStartOfDay())
    val segundos = ChronoUnit.SECONDS.between(fechaIng.atStartOfDay(), fechaAct.atStartOfDay())

    return "Tu tiempo vivido es: " + "Meses: $meses" + "Semanas: $semanas" + "Días: $dias" + "Horas: $horas" +
            "Minutos: $minutos" + "Segundos: $segundos"
}


fun main() {
    println("Bienvenido")
    print("Ingresa el Primer Numero: ")
    val num1  = readln().toInt()
    print("Ingresa el Segundo Numero: ")
    val num2 = readln().toInt()
    print("Ingresa el Tercer Numero: ")
    val num3 = readln().toInt()

    val res = suma(num1, num2, num3)
    println("La Suma de los Numeros es: $res")

    print("Ingresa tu Nombre Completo: ")
    val nom = readln()

    print("Ingresa tu Fecha de Nacimiento (formato YYYY-MM-DD): ")
    val fechaIngresada = readln()
    val resfecha = fechacalculo(fechaIngresada)
    println(resfecha)
}