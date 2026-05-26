"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Badge } from "@/components/ui/badge"
import { Calendar } from "@/components/ui/calendar"
import {
  ArrowLeft,
  CalendarIcon,
  Clock,
  Phone,
  PawPrint,
  CheckCircle,
  AlertCircle,
  User,
  Stethoscope,
  FileText,
  Plus,
  Filter,
} from "lucide-react"
import Image from "next/image"
import Link from "next/link"
import { useParams, useRouter } from "next/navigation"

// Mock data for appointments
const appointmentsData = {
  1: {
    veterinarian: {
      id: 1,
      name: "Dr. María González",
      speciality: "GENERAL",
      image: "/placeholder.svg?height=200&width=200",
    },
    appointments: [
      {
        id: 1,
        date: new Date("2024-02-15"),
        time: "09:00",
        petName: "Luna",
        petId: 1,
        ownerName: "María García",
        ownerPhone: "+57 300 111 2222",
        reason: "checkup",
        status: "CONFIRMED",
        notes: "Revisión general de salud, vacunación pendiente",
        duration: 30,
      },
      {
        id: 2,
        date: new Date("2024-02-15"),
        time: "10:00",
        petName: "Max",
        petId: 2,
        ownerName: "Juan Rodríguez",
        ownerPhone: "+57 300 333 4444",
        reason: "vaccination",
        status: "PENDING",
        notes: "Refuerzo de vacunas anuales",
        duration: 20,
      },
      {
        id: 3,
        date: new Date("2024-02-16"),
        time: "14:00",
        petName: "Bella",
        petId: 3,
        ownerName: "Ana Martínez",
        ownerPhone: "+57 300 555 6666",
        reason: "surgery",
        status: "CONFIRMED",
        notes: "Esterilización programada",
        duration: 60,
      },
      {
        id: 4,
        date: new Date("2024-02-16"),
        time: "16:00",
        petName: null,
        petId: null,
        ownerName: "Carlos López",
        ownerPhone: "+57 300 777 8888",
        reason: "consultation",
        status: "COMPLETED",
        notes: "Consulta sobre adopción responsable",
        duration: 45,
      },
      {
        id: 5,
        date: new Date("2024-02-17"),
        time: "09:30",
        petName: "Rocky",
        petId: 4,
        ownerName: "Refugio Esperanza",
        ownerPhone: "+57 301 234 5678",
        reason: "emergency",
        status: "URGENT",
        notes: "Emergencia - posible intoxicación",
        duration: 90,
      },
    ],
  },
}

const reasonLabels = {
  checkup: "Revisión General",
  vaccination: "Vacunación",
  emergency: "Emergencia",
  surgery: "Cirugía",
  followup: "Seguimiento",
  consultation: "Consulta Especializada",
  adoption: "Evaluación para Adopción",
  other: "Otro",
}

const statusLabels = {
  CONFIRMED: { label: "Confirmada", color: "bg-green-600", icon: CheckCircle },
  PENDING: { label: "Pendiente", color: "bg-yellow-600", icon: Clock },
  COMPLETED: { label: "Completada", color: "bg-blue-600", icon: CheckCircle },
  CANCELLED: { label: "Cancelada", color: "bg-red-600", icon: AlertCircle },
  URGENT: { label: "Urgente", color: "bg-red-600", icon: AlertCircle },
}

function getStatusBadge(status: string) {
  const config = statusLabels[status as keyof typeof statusLabels] || statusLabels.PENDING
  const Icon = config.icon

  return (
    <Badge className={`${config.color} text-white`}>
      <Icon className="h-3 w-3 mr-1" />
      {config.label}
    </Badge>
  )
}

export default function VeterinarianAppointmentsPage() {
  const params = useParams()
  const router = useRouter()
  const vetId = Number.parseInt(params.id as string)
  const data = appointmentsData[vetId as keyof typeof appointmentsData]
  const [selectedDate, setSelectedDate] = useState<Date | undefined>(new Date())
  const [activeTab, setActiveTab] = useState("calendar")
  const [statusFilter, setStatusFilter] = useState("all")

  if (!data) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-orange-50 to-amber-50 flex items-center justify-center">
        <div className="text-center">
          <AlertCircle className="h-16 w-16 text-orange-600 mx-auto mb-4" />
          <h2 className="text-2xl font-bold text-orange-800 mb-2">Veterinario no encontrado</h2>
          <Button asChild>
            <Link href="/shelter">
              <ArrowLeft className="h-4 w-4 mr-2" />
              Volver a Refugios
            </Link>
          </Button>
        </div>
      </div>
    )
  }

  const filteredAppointments = data.appointments.filter((appointment) => {
    if (statusFilter === "all") return true
    return appointment.status === statusFilter
  })

  const todayAppointments = data.appointments.filter(
    (appointment) => appointment.date.toDateString() === new Date().toDateString(),
  )

  const upcomingAppointments = data.appointments.filter(
    (appointment) => appointment.date > new Date() && appointment.status !== "COMPLETED",
  )

  return (
    <div className="min-h-screen bg-gradient-to-br from-orange-50 to-amber-50">
      {/* Header */}
      <header className="bg-white/90 backdrop-blur-md border-b border-orange-100 sticky top-0 z-50 shadow-sm">
        <div className="container mx-auto px-4 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-4">
              <Button
                variant="ghost"
                size="sm"
                onClick={() => router.back()}
                className="text-orange-700 hover:text-orange-900"
              >
                <ArrowLeft className="h-4 w-4 mr-2" />
                Volver
              </Button>
              <div className="h-6 w-px bg-orange-200"></div>
              <div className="flex items-center space-x-3">
                <div className="bg-orange-100 p-2 rounded-full">
                  <PawPrint className="h-6 w-6 text-orange-600" />
                </div>
                <h1 className="text-2xl font-bold text-orange-800">Sistema de Veterinaria</h1>
              </div>
            </div>
            <Button className="bg-blue-600 hover:bg-blue-700 text-white shadow-lg hover:shadow-xl transition-all duration-300">
              <Plus className="mr-2 h-4 w-4" />
              Nueva Cita
            </Button>
          </div>
        </div>
      </header>

      {/* Veterinarian Header */}
      <section className="py-8 bg-white/70 backdrop-blur-sm">
        <div className="container mx-auto px-4">
          <div className="flex items-center space-x-6">
            <Image
              src={data.veterinarian.image || "/placeholder.svg"}
              alt={data.veterinarian.name}
              width={80}
              height={80}
              className="w-20 h-20 rounded-full object-cover border-4 border-blue-200"
            />
            <div>
              <h2 className="text-3xl font-bold text-blue-800">{data.veterinarian.name}</h2>
              <p className="text-blue-600 text-lg">Agenda de Citas</p>
              <div className="flex items-center space-x-4 mt-2">
                <Badge className="bg-blue-600 text-white">
                  <Stethoscope className="h-3 w-3 mr-1" />
                  Medicina General
                </Badge>
                <span className="text-sm text-gray-600">{data.appointments.length} citas programadas</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Quick Stats */}
      <section className="py-6 bg-blue-50/50">
        <div className="container mx-auto px-4">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div className="text-center bg-white p-4 rounded-lg shadow-sm">
              <div className="text-2xl font-bold text-blue-600">{todayAppointments.length}</div>
              <div className="text-sm text-blue-800">Hoy</div>
            </div>
            <div className="text-center bg-white p-4 rounded-lg shadow-sm">
              <div className="text-2xl font-bold text-green-600">{upcomingAppointments.length}</div>
              <div className="text-sm text-green-800">Próximas</div>
            </div>
            <div className="text-center bg-white p-4 rounded-lg shadow-sm">
              <div className="text-2xl font-bold text-yellow-600">
                {data.appointments.filter((a) => a.status === "PENDING").length}
              </div>
              <div className="text-sm text-yellow-800">Pendientes</div>
            </div>
            <div className="text-center bg-white p-4 rounded-lg shadow-sm">
              <div className="text-2xl font-bold text-red-600">
                {data.appointments.filter((a) => a.status === "URGENT").length}
              </div>
              <div className="text-sm text-red-800">Urgentes</div>
            </div>
          </div>
        </div>
      </section>

      {/* Main Content */}
      <section className="py-8 px-4">
        <div className="container mx-auto">
          <Tabs value={activeTab} onValueChange={setActiveTab} className="w-full">
            <TabsList className="grid w-full grid-cols-3 max-w-md mx-auto mb-8 bg-blue-100/80 backdrop-blur-sm shadow-lg">
              <TabsTrigger
                value="calendar"
                className="data-[state=active]:bg-blue-600 data-[state=active]:text-white transition-all duration-300"
              >
                <CalendarIcon className="h-4 w-4 mr-1" />
                Calendario
              </TabsTrigger>
              <TabsTrigger
                value="list"
                className="data-[state=active]:bg-blue-600 data-[state=active]:text-white transition-all duration-300"
              >
                <FileText className="h-4 w-4 mr-1" />
                Lista
              </TabsTrigger>
              <TabsTrigger
                value="today"
                className="data-[state=active]:bg-blue-600 data-[state=active]:text-white transition-all duration-300"
              >
                <Clock className="h-4 w-4 mr-1" />
                Hoy
              </TabsTrigger>
            </TabsList>

            {/* Calendar View */}
            <TabsContent value="calendar" className="space-y-6">
              <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                <Card className="lg:col-span-1 border-blue-200">
                  <CardHeader>
                    <CardTitle className="text-blue-800">Calendario</CardTitle>
                  </CardHeader>
                  <CardContent>
                    <Calendar
                      mode="single"
                      selected={selectedDate}
                      onSelect={setSelectedDate}
                      className="rounded-md border border-blue-200"
                    />
                  </CardContent>
                </Card>

                <div className="lg:col-span-2 space-y-4">
                  <div className="flex items-center justify-between">
                    <h3 className="text-xl font-bold text-blue-800">
                      Citas para{" "}
                      {selectedDate?.toLocaleDateString("es-ES", {
                        weekday: "long",
                        year: "numeric",
                        month: "long",
                        day: "numeric",
                      })}
                    </h3>
                  </div>

                  {data.appointments
                    .filter(
                      (appointment) => selectedDate && appointment.date.toDateString() === selectedDate.toDateString(),
                    )
                    .sort((a, b) => a.time.localeCompare(b.time))
                    .map((appointment) => (
                      <Card
                        key={appointment.id}
                        className="border-blue-200 hover:shadow-lg transition-all duration-300"
                      >
                        <CardContent className="p-4">
                          <div className="flex items-start justify-between mb-3">
                            <div className="flex items-center space-x-3">
                              <div className="bg-blue-100 p-2 rounded-full">
                                <Clock className="h-4 w-4 text-blue-600" />
                              </div>
                              <div>
                                <h4 className="font-semibold text-blue-800">{appointment.time}</h4>
                                <p className="text-sm text-gray-600">{appointment.duration} minutos</p>
                              </div>
                            </div>
                            {getStatusBadge(appointment.status)}
                          </div>

                          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                              <p className="text-sm font-medium text-gray-700">Motivo:</p>
                              <p className="text-blue-800">
                                {reasonLabels[appointment.reason as keyof typeof reasonLabels]}
                              </p>
                            </div>
                            <div>
                              <p className="text-sm font-medium text-gray-700">Cliente:</p>
                              <p className="text-blue-800">{appointment.ownerName}</p>
                            </div>
                            {appointment.petName && (
                              <div>
                                <p className="text-sm font-medium text-gray-700">Mascota:</p>
                                <p className="text-blue-800">{appointment.petName}</p>
                              </div>
                            )}
                            <div>
                              <p className="text-sm font-medium text-gray-700">Teléfono:</p>
                              <p className="text-blue-800">{appointment.ownerPhone}</p>
                            </div>
                          </div>

                          {appointment.notes && (
                            <div className="mt-3 bg-blue-50 p-3 rounded-lg">
                              <p className="text-sm font-medium text-blue-800 mb-1">Notas:</p>
                              <p className="text-sm text-blue-700">{appointment.notes}</p>
                            </div>
                          )}
                        </CardContent>
                      </Card>
                    ))}

                  {data.appointments.filter(
                    (appointment) => selectedDate && appointment.date.toDateString() === selectedDate.toDateString(),
                  ).length === 0 && (
                    <Card className="border-gray-200">
                      <CardContent className="p-8 text-center">
                        <CalendarIcon className="h-12 w-12 text-gray-400 mx-auto mb-4" />
                        <p className="text-gray-600">No hay citas programadas para este día</p>
                      </CardContent>
                    </Card>
                  )}
                </div>
              </div>
            </TabsContent>

            {/* List View */}
            <TabsContent value="list" className="space-y-6">
              <div className="flex items-center justify-between">
                <h3 className="text-2xl font-bold text-blue-800">Todas las Citas</h3>
                <div className="flex items-center space-x-2">
                  <Filter className="h-4 w-4 text-gray-600" />
                  <select
                    value={statusFilter}
                    onChange={(e) => setStatusFilter(e.target.value)}
                    className="border border-blue-200 rounded-md px-3 py-1 text-sm"
                  >
                    <option value="all">Todos los estados</option>
                    <option value="CONFIRMED">Confirmadas</option>
                    <option value="PENDING">Pendientes</option>
                    <option value="COMPLETED">Completadas</option>
                    <option value="URGENT">Urgentes</option>
                  </select>
                </div>
              </div>

              <div className="space-y-4">
                {filteredAppointments
                  .sort((a, b) => a.date.getTime() - b.date.getTime())
                  .map((appointment) => (
                    <Card key={appointment.id} className="border-blue-200 hover:shadow-lg transition-all duration-300">
                      <CardContent className="p-6">
                        <div className="flex items-start justify-between mb-4">
                          <div>
                            <h4 className="text-lg font-semibold text-blue-800">
                              {appointment.date.toLocaleDateString("es-ES")} - {appointment.time}
                            </h4>
                            <p className="text-blue-600">
                              {reasonLabels[appointment.reason as keyof typeof reasonLabels]}
                            </p>
                          </div>
                          {getStatusBadge(appointment.status)}
                        </div>

                        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                          <div className="flex items-center space-x-2">
                            <User className="h-4 w-4 text-blue-600" />
                            <div>
                              <p className="text-sm font-medium text-gray-700">Cliente</p>
                              <p className="text-blue-800">{appointment.ownerName}</p>
                            </div>
                          </div>

                          {appointment.petName && (
                            <div className="flex items-center space-x-2">
                              <PawPrint className="h-4 w-4 text-blue-600" />
                              <div>
                                <p className="text-sm font-medium text-gray-700">Mascota</p>
                                <p className="text-blue-800">{appointment.petName}</p>
                              </div>
                            </div>
                          )}

                          <div className="flex items-center space-x-2">
                            <Phone className="h-4 w-4 text-blue-600" />
                            <div>
                              <p className="text-sm font-medium text-gray-700">Teléfono</p>
                              <p className="text-blue-800">{appointment.ownerPhone}</p>
                            </div>
                          </div>
                        </div>

                        {appointment.notes && (
                          <div className="mt-4 bg-blue-50 p-3 rounded-lg">
                            <p className="text-sm font-medium text-blue-800 mb-1">Notas:</p>
                            <p className="text-sm text-blue-700">{appointment.notes}</p>
                          </div>
                        )}
                      </CardContent>
                    </Card>
                  ))}
              </div>
            </TabsContent>

            {/* Today View */}
            <TabsContent value="today" className="space-y-6">
              <div className="text-center mb-6">
                <h3 className="text-2xl font-bold text-blue-800">Citas de Hoy</h3>
                <p className="text-blue-700">
                  {new Date().toLocaleDateString("es-ES", {
                    weekday: "long",
                    year: "numeric",
                    month: "long",
                    day: "numeric",
                  })}
                </p>
              </div>

              {todayAppointments.length > 0 ? (
                <div className="space-y-4">
                  {todayAppointments
                    .sort((a, b) => a.time.localeCompare(b.time))
                    .map((appointment) => (
                      <Card
                        key={appointment.id}
                        className="border-blue-200 hover:shadow-lg transition-all duration-300"
                      >
                        <CardContent className="p-6">
                          <div className="flex items-center justify-between mb-4">
                            <div className="flex items-center space-x-4">
                              <div className="bg-blue-100 p-3 rounded-full">
                                <Clock className="h-6 w-6 text-blue-600" />
                              </div>
                              <div>
                                <h4 className="text-xl font-semibold text-blue-800">{appointment.time}</h4>
                                <p className="text-blue-600">
                                  {reasonLabels[appointment.reason as keyof typeof reasonLabels]}
                                </p>
                              </div>
                            </div>
                            {getStatusBadge(appointment.status)}
                          </div>

                          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div className="space-y-3">
                              <div className="flex items-center space-x-2">
                                <User className="h-4 w-4 text-blue-600" />
                                <span className="font-medium text-gray-700">Cliente:</span>
                                <span className="text-blue-800">{appointment.ownerName}</span>
                              </div>

                              {appointment.petName && (
                                <div className="flex items-center space-x-2">
                                  <PawPrint className="h-4 w-4 text-blue-600" />
                                  <span className="font-medium text-gray-700">Mascota:</span>
                                  <span className="text-blue-800">{appointment.petName}</span>
                                </div>
                              )}

                              <div className="flex items-center space-x-2">
                                <Phone className="h-4 w-4 text-blue-600" />
                                <span className="font-medium text-gray-700">Teléfono:</span>
                                <span className="text-blue-800">{appointment.ownerPhone}</span>
                              </div>
                            </div>

                            <div>
                              <p className="font-medium text-gray-700 mb-2">Duración:</p>
                              <p className="text-blue-800">{appointment.duration} minutos</p>
                            </div>
                          </div>

                          {appointment.notes && (
                            <div className="mt-4 bg-blue-50 p-4 rounded-lg">
                              <p className="text-sm font-medium text-blue-800 mb-2">Notas:</p>
                              <p className="text-blue-700">{appointment.notes}</p>
                            </div>
                          )}
                        </CardContent>
                      </Card>
                    ))}
                </div>
              ) : (
                <Card className="border-gray-200">
                  <CardContent className="p-12 text-center">
                    <CalendarIcon className="h-16 w-16 text-gray-400 mx-auto mb-4" />
                    <h4 className="text-xl font-semibold text-gray-600 mb-2">No hay citas para hoy</h4>
                    <p className="text-gray-500">Disfruta de tu día libre o programa nuevas citas</p>
                  </CardContent>
                </Card>
              )}
            </TabsContent>
          </Tabs>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-orange-900 text-white py-12">
        <div className="container mx-auto px-4">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
            <div>
              <div className="flex items-center space-x-2 mb-4">
                <PawPrint className="h-6 w-6" />
                <h4 className="text-xl font-bold">Sistema de Veterinaria</h4>
              </div>
              <p className="text-orange-200">Conectando corazones, creando familias.</p>
            </div>
            <div>
              <h5 className="font-semibold mb-4">Adopción</h5>
              <ul className="space-y-2 text-orange-200">
                <li>
                  <Link href="/" className="hover:text-white">
                    Mascotas Disponibles
                  </Link>
                </li>
                <li>
                  <Link href="#" className="hover:text-white">
                    Proceso de Adopción
                  </Link>
                </li>
                <li>
                  <Link href="#" className="hover:text-white">
                    Requisitos
                  </Link>
                </li>
              </ul>
            </div>
            <div>
              <h5 className="font-semibold mb-4">Refugios</h5>
              <ul className="space-y-2 text-orange-200">
                <li>
                  <Link href="/shelter" className="hover:text-white">
                    Refugios Aliados
                  </Link>
                </li>
                <li>
                  <Link href="#" className="hover:text-white">
                    Ser Refugio Aliado
                  </Link>
                </li>
                <li>
                  <Link href="#" className="hover:text-white">
                    Donaciones
                  </Link>
                </li>
              </ul>
            </div>
            <div>
              <h5 className="font-semibold mb-4">Contacto</h5>
              <ul className="space-y-2 text-orange-200">
                <li>info@avesdehermes.com</li>
                <li>+57 300 123 4567</li>
                <li>Bogotá, Colombia</li>
              </ul>
            </div>
          </div>
          <div className="border-t border-orange-800 mt-8 pt-8 text-center text-orange-200">
            <p>&copy; 2024 Sistema de Veterinaria. Todos los derechos reservados.</p>
          </div>
        </div>
      </footer>
    </div>
  )
}
