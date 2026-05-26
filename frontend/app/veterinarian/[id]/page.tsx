"use client"

import type React from "react"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Badge } from "@/components/ui/badge"
import {
  ArrowLeft,
  Stethoscope,
  Calendar,
  Clock,
  Phone,
  Mail,
  FileText,
  Heart,
  PawPrint,
  Activity,
  CheckCircle,
  AlertCircle,
  Star,
  Award,
  MapPin,
  ClipboardList,
  TrendingUp,
} from "lucide-react"
import Image from "next/image"
import Link from "next/link"
import { useParams, useRouter } from "next/navigation"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Textarea } from "@/components/ui/textarea"

// Mock data for veterinarian details
const veterinarianDetails = {
  1: {
    // Basic VeterinarianDTO info
    id: 1,
    name: "Dr. María González",
    email: "maria.gonzalez@refugioesperanza.org",
    phone: "+57 301 111 2222",
    documentType: "CC",
    documentNumber: "12345678",
    birthDate: new Date("1985-03-15"),
    licenseNumber: "VET-2018-001",
    speciality: "GENERAL",
    disponibilities: ["MORNING", "AFTERNOON"],
    yearsExperience: 8,
    image: "/placeholder.svg?height=300&width=300",
    shelter: {
      id: 1,
      name: "Refugio Esperanza",
      address: "Calle 45 #12-34, Bogotá",
      pets: [
        { id: 1, name: "Luna", breed: "Criollo" },
        { id: 2, name: "Max", breed: "Golden Retriever" },
        { id: 3, name: "Bella", breed: "Siames" },
        { id: 4, name: "Rocky", breed: "Pitbull" },
        { id: 5, name: "Mia", breed: "Persa" },
      ],
    },

    // VeterinarianDetailDTO additional info
    medicalEvents: [
      {
        id: 1,
        petName: "Luna",
        petId: 1,
        date: new Date("2024-01-20"),
        type: "CHECKUP",
        description: "Revisión general de salud",
        diagnosis: "Excelente estado de salud",
        treatment: "Continuar con rutina de ejercicio y alimentación",
        nextAppointment: new Date("2024-04-20"),
        status: "COMPLETED",
      },
      {
        id: 2,
        petName: "Max",
        petId: 2,
        date: new Date("2024-01-18"),
        type: "VACCINATION",
        description: "Refuerzo de vacunas anuales",
        diagnosis: "Vacunación completa",
        treatment: "Observación por 24 horas post-vacunación",
        status: "COMPLETED",
      },
      {
        id: 3,
        petName: "Bella",
        petId: 3,
        date: new Date("2024-01-25"),
        type: "SURGERY",
        description: "Esterilización",
        diagnosis: "Procedimiento exitoso",
        treatment: "Reposo por 10 días, antibióticos",
        status: "IN_PROGRESS",
      },
    ],

    adoptionApplications: [
      {
        id: 1,
        petName: "Luna",
        petId: 1,
        applicantName: "María García",
        applicationDate: new Date("2024-01-15"),
        status: "APPROVED",
        evaluationDate: new Date("2024-01-18"),
        notes: "Excelente candidata, experiencia previa con mascotas grandes",
        recommendation: "APPROVED",
      },
      {
        id: 2,
        petName: "Max",
        petId: 2,
        applicantName: "Juan Rodríguez",
        applicationDate: new Date("2024-01-10"),
        status: "PENDING",
        evaluationDate: null,
        notes: "Pendiente evaluación domiciliaria",
        recommendation: "PENDING",
      },
    ],

    followUps: [
      {
        id: 1,
        petName: "Rocky",
        petId: 4,
        ownerName: "Carlos Mendoza",
        adoptionDate: new Date("2023-12-01"),
        followUpDate: new Date("2024-01-15"),
        status: "EXCELLENT",
        notes: "Mascota completamente adaptada, excelente cuidado",
        nextFollowUp: new Date("2024-04-15"),
        monthsPostAdoption: 1.5,
      },
      {
        id: 2,
        petName: "Mia",
        petId: 3,
        ownerName: "Ana Martínez",
        adoptionDate: new Date("2023-11-15"),
        followUpDate: new Date("2024-01-10"),
        status: "GOOD",
        notes: "Adaptación positiva, algunas recomendaciones de comportamiento",
        nextFollowUp: new Date("2024-03-10"),
        monthsPostAdoption: 2,
      },
    ],

    adoptions: [
      {
        id: 1,
        petName: "Rocky",
        petId: 4,
        ownerName: "Carlos Mendoza",
        adoptionDate: new Date("2023-12-01"),
        status: "COMPLETED",
        finalApproval: true,
        notes: "Adopción exitosa, seguimiento programado",
      },
      {
        id: 2,
        petName: "Mia",
        petId: 3,
        ownerName: "Ana Martínez",
        adoptionDate: new Date("2023-11-15"),
        status: "COMPLETED",
        finalApproval: true,
        notes: "Proceso completo, excelente match",
      },
    ],

    adoptionTests: [
      {
        id: 1,
        petName: "Luna",
        petId: 1,
        applicantName: "María García",
        testDate: new Date("2024-01-16"),
        testType: "BEHAVIORAL",
        result: "PASSED",
        score: 85,
        notes: "Excelente interacción, mascota muy receptiva",
        recommendations: "Continuar con proceso de adopción",
      },
      {
        id: 2,
        petName: "Max",
        petId: 2,
        applicantName: "Juan Rodríguez",
        testDate: new Date("2024-01-12"),
        testType: "COMPATIBILITY",
        result: "PASSED",
        score: 78,
        notes: "Buena compatibilidad, requiere período de adaptación",
        recommendations: "Visitas graduales antes de adopción",
      },
    ],

    shelterArrivals: [
      {
        id: 1,
        petName: "Luna",
        petId: 1,
        arrivalDate: new Date("2023-08-10"),
        reason: "ABANDONMENT",
        condition: "GOOD",
        initialExamDate: new Date("2023-08-10"),
        initialDiagnosis: "Buen estado general, requiere vacunación",
        treatment: "Vacunación completa, desparasitación",
        notes: "Mascota sociable, sin problemas de comportamiento",
      },
      {
        id: 2,
        petName: "Bella",
        petId: 3,
        arrivalDate: new Date("2023-09-15"),
        reason: "RESCUE",
        condition: "POOR",
        initialExamDate: new Date("2023-09-15"),
        initialDiagnosis: "Desnutrición, problemas de piel",
        treatment: "Tratamiento nutricional, medicación dermatológica",
        notes: "Requirió cuidados intensivos, recuperación exitosa",
      },
    ],

    // Statistics
    stats: {
      totalMedicalEvents: 45,
      totalAdoptions: 23,
      totalFollowUps: 18,
      totalArrivals: 12,
      successRate: 95,
      averageScore: 82,
    },
  },
}

const specialityLabels = {
  GENERAL: "Medicina General",
  SURGERY: "Cirugía",
  DERMATOLOGY: "Dermatología",
  OPHTHALMOLOGY: "Oftalmología",
  CARDIOLOGY: "Cardiología",
  NEUROLOGY: "Neurología",
  ONCOLOGY: "Oncología",
  INTERNAL_MEDICINE: "Medicina Interna",
  INFECTOLOGY: "Infectología",
  ORTHOPEDICS: "Ortopedia",
  REPRODUCTION: "Reproducción",
  NUTRITION: "Nutrición",
  BEHAVIOR: "Comportamiento",
  PHYSIOTHERAPY: "Fisioterapia",
}

const disponibilityLabels = {
  MORNING: "Mañana",
  AFTERNOON: "Tarde",
  EVENING: "Noche",
  FULL_TIME: "Tiempo Completo",
}

const timeSlots = {
  MORNING: ["9:00 AM", "10:00 AM", "11:00 AM"],
  AFTERNOON: ["2:00 PM", "3:00 PM", "4:00 PM"],
}

const getStatusBadge = (status: string, type: string) => {
  switch (type) {
    case "medical":
      return (
        <Badge
          className={
            status === "COMPLETED"
              ? "bg-green-600 text-white"
              : status === "IN_PROGRESS"
                ? "bg-yellow-600 text-white"
                : "bg-red-600 text-white"
          }
        >
          {status === "COMPLETED" ? "Completado" : status === "IN_PROGRESS" ? "En Progreso" : "Pendiente"}
        </Badge>
      )
    case "adoption":
      return (
        <Badge
          className={
            status === "APPROVED"
              ? "bg-green-600 text-white"
              : status === "REJECTED"
                ? "bg-red-600 text-white"
                : "bg-yellow-600 text-white"
          }
        >
          {status === "APPROVED" ? "Aprobada" : status === "REJECTED" ? "Rechazada" : "Pendiente"}
        </Badge>
      )
    case "followup":
      return (
        <Badge
          className={
            status === "EXCELLENT"
              ? "bg-green-600 text-white"
              : status === "GOOD"
                ? "bg-yellow-600 text-white"
                : "bg-red-600 text-white"
          }
        >
          {status === "EXCELLENT" ? "Excelente" : status === "GOOD" ? "Regular" : "Crítico"}
        </Badge>
      )
    case "test":
      return (
        <Badge className={status === "PASSED" ? "bg-green-600 text-white" : "bg-red-600 text-white"}>
          {status === "PASSED" ? "Aprobado" : "Rechazado"}
        </Badge>
      )
    default:
      return null
  }
}

const handleAppointmentSubmit = async (e: React.FormEvent) => {
  e.preventDefault()
  // Simulate API call
  await new Promise((resolve) => setTimeout(resolve, 1000))

  // Here you would submit to your backend
  const payload = {
    ...appointmentForm,
    petId: appointmentForm.petId === "none" ? null : Number(appointmentForm.petId),
  }

  console.log("Appointment scheduled:", payload)

  setShowAppointmentDialog(false)
  setAppointmentForm({
    date: "",
    time: "",
    reason: "",
    petId: "none",
    notes: "",
  })

  // Show success message (you could use a toast here)
  alert("¡Cita agendada exitosamente!")
}

const getAvailableTimeSlots = (vetId: number) => {
  const slots: string[] = []
  const vet = veterinarianDetails[vetId as keyof typeof veterinarianDetails]
  vet.disponibilities.forEach((period) => {
    if (timeSlots[period as keyof typeof timeSlots]) {
      slots.push(...timeSlots[period as keyof typeof timeSlots])
    }
  })
  return slots.sort()
}

export default function VeterinarianDetailPage() {
  const params = useParams()
  const router = useRouter()
  const vetId = Number.parseInt(params.id as string)
  const vet = veterinarianDetails[vetId as keyof typeof veterinarianDetails]
  const [activeTab, setActiveTab] = useState("overview")
  const [showAppointmentDialog, setShowAppointmentDialog] = useState(false)
  const [appointmentForm, setAppointmentForm] = useState({
    date: "",
    time: "",
    reason: "",
    petId: "none",
    notes: "",
  })

  const shelter = vet.shelter

  if (!vet) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-orange-50 to-amber-50 flex items-center justify-center">
        <div className="text-center">
          <AlertCircle className="h-16 w-16 text-orange-600 mx-auto mb-4" />
          <h2 className="text-2xl font-bold text-orange-800 mb-2">Veterinario no encontrado</h2>
          <p className="text-orange-700 mb-4">El veterinario que buscas no existe.</p>
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
            <Button
              onClick={() => setShowAppointmentDialog(true)}
              className="bg-blue-600 hover:bg-blue-700 text-white shadow-lg hover:shadow-xl transition-all duration-300"
            >
              <Calendar className="mr-2 h-4 w-4" />
              Agendar Cita
            </Button>
          </div>
        </div>
      </header>

      {/* Veterinarian Profile Header */}
      <section className="py-12 px-4">
        <div className="container mx-auto">
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            {/* Profile Card */}
            <div className="lg:col-span-1">
              <Card className="border-orange-200 shadow-xl sticky top-24">
                <CardHeader className="text-center bg-gradient-to-r from-blue-50 to-indigo-50">
                  <div className="mx-auto mb-4 relative">
                    <div className="absolute -inset-2 bg-gradient-to-r from-blue-400 to-indigo-400 rounded-full opacity-20"></div>
                    <Image
                      src={vet.image || "/placeholder.svg"}
                      alt={vet.name}
                      width={300}
                      height={300}
                      className="w-32 h-32 rounded-full mx-auto object-cover relative z-10 border-4 border-white shadow-lg"
                    />
                  </div>
                  <CardTitle className="text-2xl text-blue-800">{vet.name}</CardTitle>
                  <CardDescription className="text-blue-600 text-lg">
                    {specialityLabels[vet.speciality as keyof typeof specialityLabels]}
                  </CardDescription>
                  <div className="flex justify-center mt-2">
                    <Badge className="bg-blue-600 text-white">
                      <Stethoscope className="h-3 w-3 mr-1" />
                      Licencia: {vet.licenseNumber}
                    </Badge>
                  </div>
                </CardHeader>
                <CardContent className="p-6 space-y-4">
                  <div className="space-y-3">
                    <div className="flex items-center text-sm text-gray-600">
                      <Phone className="h-4 w-4 mr-3 text-blue-600" />
                      <span>{vet.phone}</span>
                    </div>
                    <div className="flex items-center text-sm text-gray-600">
                      <Mail className="h-4 w-4 mr-3 text-blue-600" />
                      <span>{vet.email}</span>
                    </div>
                    <div className="flex items-center text-sm text-gray-600">
                      <Star className="h-4 w-4 mr-3 text-blue-600" />
                      <span>{vet.yearsExperience} años de experiencia</span>
                    </div>
                    <div className="flex items-center text-sm text-gray-600">
                      <MapPin className="h-4 w-4 mr-3 text-blue-600" />
                      <span>{vet.shelter.name}</span>
                    </div>
                  </div>

                  <div className="border-t pt-4">
                    <h4 className="font-semibold text-blue-800 mb-3">Disponibilidad</h4>
                    <div className="flex flex-wrap gap-2">
                      {vet.disponibilities.map((disp, index) => (
                        <Badge key={index} variant="outline" className="border-blue-600 text-blue-600">
                          <Clock className="h-3 w-3 mr-1" />
                          {disponibilityLabels[disp as keyof typeof disponibilityLabels]}
                        </Badge>
                      ))}
                    </div>
                  </div>

                  <div className="border-t pt-4">
                    <h4 className="font-semibold text-blue-800 mb-3">Estadísticas</h4>
                    <div className="grid grid-cols-2 gap-3">
                      <div className="text-center bg-green-50 p-2 rounded">
                        <div className="text-lg font-bold text-green-600">{vet.stats.totalAdoptions}</div>
                        <div className="text-xs text-green-700">Adopciones</div>
                      </div>
                      <div className="text-center bg-blue-50 p-2 rounded">
                        <div className="text-lg font-bold text-blue-600">{vet.stats.totalMedicalEvents}</div>
                        <div className="text-xs text-blue-700">Consultas</div>
                      </div>
                      <div className="text-center bg-purple-50 p-2 rounded">
                        <div className="text-lg font-bold text-purple-600">{vet.stats.successRate}%</div>
                        <div className="text-xs text-purple-700">Éxito</div>
                      </div>
                      <div className="text-center bg-orange-50 p-2 rounded">
                        <div className="text-lg font-bold text-orange-600">{vet.stats.averageScore}</div>
                        <div className="text-xs text-orange-700">Puntuación</div>
                      </div>
                    </div>
                  </div>
                </CardContent>
              </Card>
            </div>

            {/* Main Content */}
            <div className="lg:col-span-2">
              <Tabs value={activeTab} onValueChange={setActiveTab} className="w-full">
                <TabsList className="grid w-full grid-cols-6 mb-8 bg-blue-100/80 backdrop-blur-sm shadow-lg">
                  <TabsTrigger
                    value="overview"
                    className="data-[state=active]:bg-blue-600 data-[state=active]:text-white transition-all duration-300 text-xs"
                  >
                    <Activity className="h-3 w-3 mr-1" />
                    Resumen
                  </TabsTrigger>
                  <TabsTrigger
                    value="medical"
                    className="data-[state=active]:bg-blue-600 data-[state=active]:text-white transition-all duration-300 text-xs"
                  >
                    <Stethoscope className="h-3 w-3 mr-1" />
                    Médico
                  </TabsTrigger>
                  <TabsTrigger
                    value="applications"
                    className="data-[state=active]:bg-blue-600 data-[state=active]:text-white transition-all duration-300 text-xs"
                  >
                    <FileText className="h-3 w-3 mr-1" />
                    Solicitudes
                  </TabsTrigger>
                  <TabsTrigger
                    value="followups"
                    className="data-[state=active]:bg-blue-600 data-[state=active]:text-white transition-all duration-300 text-xs"
                  >
                    <TrendingUp className="h-3 w-3 mr-1" />
                    Seguimientos
                  </TabsTrigger>
                  <TabsTrigger
                    value="tests"
                    className="data-[state=active]:bg-blue-600 data-[state=active]:text-white transition-all duration-300 text-xs"
                  >
                    <ClipboardList className="h-3 w-3 mr-1" />
                    Tests
                  </TabsTrigger>
                  <TabsTrigger
                    value="arrivals"
                    className="data-[state=active]:bg-blue-600 data-[state=active]:text-white transition-all duration-300 text-xs"
                  >
                    <PawPrint className="h-3 w-3 mr-1" />
                    Llegadas
                  </TabsTrigger>
                </TabsList>

                {/* Overview Tab */}
                <TabsContent value="overview" className="space-y-6">
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <Card className="border-blue-200">
                      <CardHeader>
                        <CardTitle className="text-blue-800 flex items-center">
                          <Activity className="h-5 w-5 mr-2" />
                          Actividad Reciente
                        </CardTitle>
                      </CardHeader>
                      <CardContent className="space-y-3">
                        {vet.medicalEvents.slice(0, 3).map((event) => (
                          <div key={event.id} className="flex items-center justify-between p-3 bg-blue-50 rounded-lg">
                            <div>
                              <p className="font-medium text-blue-800">{event.petName}</p>
                              <p className="text-sm text-blue-600">{event.description}</p>
                              <p className="text-xs text-gray-500">{event.date.toLocaleDateString()}</p>
                            </div>
                            {getStatusBadge(event.status, "medical")}
                          </div>
                        ))}
                      </CardContent>
                    </Card>

                    <Card className="border-green-200">
                      <CardHeader>
                        <CardTitle className="text-green-800 flex items-center">
                          <Heart className="h-5 w-5 mr-2" />
                          Adopciones Recientes
                        </CardTitle>
                      </CardHeader>
                      <CardContent className="space-y-3">
                        {vet.adoptions.slice(0, 3).map((adoption) => (
                          <div
                            key={adoption.id}
                            className="flex items-center justify-between p-3 bg-green-50 rounded-lg"
                          >
                            <div>
                              <p className="font-medium text-green-800">{adoption.petName}</p>
                              <p className="text-sm text-green-600">Adoptado por {adoption.ownerName}</p>
                              <p className="text-xs text-gray-500">{adoption.adoptionDate.toLocaleDateString()}</p>
                            </div>
                            <Badge className="bg-green-600 text-white">
                              <CheckCircle className="h-3 w-3 mr-1" />
                              Exitosa
                            </Badge>
                          </div>
                        ))}
                      </CardContent>
                    </Card>
                  </div>

                  <Card className="border-purple-200">
                    <CardHeader>
                      <CardTitle className="text-purple-800 flex items-center">
                        <Award className="h-5 w-5 mr-2" />
                        Rendimiento General
                      </CardTitle>
                    </CardHeader>
                    <CardContent>
                      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                        <div className="text-center p-4 bg-blue-50 rounded-lg">
                          <Stethoscope className="h-8 w-8 text-blue-600 mx-auto mb-2" />
                          <div className="text-2xl font-bold text-blue-600">{vet.stats.totalMedicalEvents}</div>
                          <div className="text-sm text-blue-700">Eventos Médicos</div>
                        </div>
                        <div className="text-center p-4 bg-green-50 rounded-lg">
                          <Heart className="h-8 w-8 text-green-600 mx-auto mb-2" />
                          <div className="text-2xl font-bold text-green-600">{vet.stats.totalAdoptions}</div>
                          <div className="text-sm text-green-700">Adopciones</div>
                        </div>
                        <div className="text-center p-4 bg-purple-50 rounded-lg">
                          <TrendingUp className="h-8 w-8 text-purple-600 mx-auto mb-2" />
                          <div className="text-2xl font-bold text-purple-600">{vet.stats.totalFollowUps}</div>
                          <div className="text-sm text-purple-700">Seguimientos</div>
                        </div>
                        <div className="text-center p-4 bg-orange-50 rounded-lg">
                          <PawPrint className="h-8 w-8 text-orange-600 mx-auto mb-2" />
                          <div className="text-2xl font-bold text-orange-600">{vet.stats.totalArrivals}</div>
                          <div className="text-sm text-orange-700">Llegadas</div>
                        </div>
                      </div>
                    </CardContent>
                  </Card>
                </TabsContent>

                {/* Medical Events Tab */}
                <TabsContent value="medical" className="space-y-6">
                  <div className="text-center mb-6">
                    <h3 className="text-2xl font-bold text-blue-800 mb-2">Eventos Médicos</h3>
                    <p className="text-blue-700">Historial completo de consultas y procedimientos realizados</p>
                  </div>

                  <div className="space-y-4">
                    {vet.medicalEvents.map((event) => (
                      <Card key={event.id} className="border-blue-200 hover:shadow-lg transition-all duration-300">
                        <CardContent className="p-6">
                          <div className="flex items-start justify-between mb-4">
                            <div>
                              <h4 className="text-lg font-semibold text-blue-800">{event.description}</h4>
                              <p className="text-blue-600">
                                Paciente:{" "}
                                <Link href={`/pet/${event.petId}`} className="hover:underline font-medium">
                                  {event.petName}
                                </Link>
                              </p>
                              <p className="text-gray-600">{event.date.toLocaleDateString()}</p>
                            </div>
                            {getStatusBadge(event.status, "medical")}
                          </div>

                          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                              <h5 className="font-medium text-gray-800 mb-2">Diagnóstico</h5>
                              <p className="text-gray-600 text-sm">{event.diagnosis}</p>
                            </div>
                            <div>
                              <h5 className="font-medium text-gray-800 mb-2">Tratamiento</h5>
                              <p className="text-gray-600 text-sm">{event.treatment}</p>
                            </div>
                          </div>

                          {event.nextAppointment && (
                            <div className="mt-4 bg-blue-50 p-3 rounded-lg">
                              <div className="flex items-center text-blue-700">
                                <Calendar className="h-4 w-4 mr-2" />
                                <span className="font-medium">
                                  Próxima cita: {event.nextAppointment.toLocaleDateString()}
                                </span>
                              </div>
                            </div>
                          )}
                        </CardContent>
                      </Card>
                    ))}
                  </div>
                </TabsContent>

                {/* Adoption Applications Tab */}
                <TabsContent value="applications" className="space-y-6">
                  <div className="text-center mb-6">
                    <h3 className="text-2xl font-bold text-blue-800 mb-2">Solicitudes de Adopción</h3>
                    <p className="text-blue-700">Evaluaciones y recomendaciones para procesos de adopción</p>
                  </div>

                  <div className="space-y-4">
                    {vet.adoptionApplications.map((application) => (
                      <Card
                        key={application.id}
                        className="border-blue-200 hover:shadow-lg transition-all duration-300"
                      >
                        <CardContent className="p-6">
                          <div className="flex items-start justify-between mb-4">
                            <div>
                              <h4 className="text-lg font-semibold text-blue-800">{application.applicantName}</h4>
                              <p className="text-blue-600">
                                Mascota:{" "}
                                <Link href={`/pet/${application.petId}`} className="hover:underline font-medium">
                                  {application.petName}
                                </Link>
                              </p>
                              <p className="text-gray-600">
                                Solicitud: {application.applicationDate.toLocaleDateString()}
                              </p>
                            </div>
                            {getStatusBadge(application.status, "adoption")}
                          </div>

                          <div className="space-y-3">
                            {application.evaluationDate && (
                              <div className="flex items-center text-sm text-gray-600">
                                <Calendar className="h-4 w-4 mr-2 text-blue-600" />
                                <span>Evaluación: {application.evaluationDate.toLocaleDateString()}</span>
                              </div>
                            )}

                            <div className="bg-blue-50 p-3 rounded-lg">
                              <h5 className="font-medium text-blue-800 mb-2">Notas de Evaluación</h5>
                              <p className="text-blue-700 text-sm">{application.notes}</p>
                            </div>

                            <div className="flex items-center justify-between">
                              <span className="text-sm font-medium text-gray-700">Recomendación:</span>
                              <Badge
                                className={
                                  application.recommendation === "APPROVED"
                                    ? "bg-green-600"
                                    : application.recommendation === "REJECTED"
                                      ? "bg-red-600"
                                      : "bg-yellow-600"
                                }
                              >
                                {application.recommendation === "APPROVED"
                                  ? "Aprobada"
                                  : application.recommendation === "REJECTED"
                                    ? "Rechazada"
                                    : "Pendiente"}
                              </Badge>
                            </div>
                          </div>
                        </CardContent>
                      </Card>
                    ))}
                  </div>
                </TabsContent>

                {/* Follow-ups Tab */}
                <TabsContent value="followups" className="space-y-6">
                  <div className="text-center mb-6">
                    <h3 className="text-2xl font-bold text-blue-800 mb-2">Seguimientos Post-Adopción</h3>
                    <p className="text-blue-700">Monitoreo del bienestar de mascotas adoptadas</p>
                  </div>

                  <div className="space-y-4">
                    {vet.followUps.map((followUp) => (
                      <Card key={followUp.id} className="border-blue-200 hover:shadow-lg transition-all duration-300">
                        <CardContent className="p-6">
                          <div className="flex items-start justify-between mb-4">
                            <div>
                              <h4 className="text-lg font-semibold text-blue-800">{followUp.petName}</h4>
                              <p className="text-blue-600">Adoptante: {followUp.ownerName}</p>
                              <p className="text-gray-600">
                                Adopción: {followUp.adoptionDate.toLocaleDateString()} ({followUp.monthsPostAdoption}{" "}
                                meses)
                              </p>
                            </div>
                            {getStatusBadge(followUp.status, "followup")}
                          </div>

                          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                            <div className="flex items-center text-sm text-gray-600">
                              <Calendar className="h-4 w-4 mr-2 text-blue-600" />
                              <span>Seguimiento: {followUp.followUpDate.toLocaleDateString()}</span>
                            </div>
                            <div className="flex items-center text-sm text-gray-600">
                              <Clock className="h-4 w-4 mr-2 text-blue-600" />
                              <span>Próximo: {followUp.nextFollowUp.toLocaleDateString()}</span>
                            </div>
                          </div>

                          <div className="bg-green-50 p-3 rounded-lg">
                            <h5 className="font-medium text-green-800 mb-2">Observaciones</h5>
                            <p className="text-green-700 text-sm">{followUp.notes}</p>
                          </div>
                        </CardContent>
                      </Card>
                    ))}
                  </div>
                </TabsContent>

                {/* Adoption Tests Tab */}
                <TabsContent value="tests" className="space-y-6">
                  <div className="text-center mb-6">
                    <h3 className="text-2xl font-bold text-blue-800 mb-2">Tests de Adopción</h3>
                    <p className="text-blue-700">Evaluaciones de compatibilidad y comportamiento</p>
                  </div>

                  <div className="space-y-4">
                    {vet.adoptionTests.map((test) => (
                      <Card key={test.id} className="border-blue-200 hover:shadow-lg transition-all duration-300">
                        <CardContent className="p-6">
                          <div className="flex items-start justify-between mb-4">
                            <div>
                              <h4 className="text-lg font-semibold text-blue-800">
                                {test.testType === "BEHAVIORAL" ? "Test Comportamental" : "Test de Compatibilidad"}
                              </h4>
                              <p className="text-blue-600">
                                Mascota:{" "}
                                <Link href={`/pet/${test.petId}`} className="hover:underline font-medium">
                                  {test.petName}
                                </Link>
                              </p>
                              <p className="text-blue-600">Aplicante: {test.applicantName}</p>
                              <p className="text-gray-600">{test.testDate.toLocaleDateString()}</p>
                            </div>
                            <div className="text-right">
                              {getStatusBadge(test.result, "test")}
                              <div className="mt-2">
                                <Badge variant="outline" className="border-blue-600 text-blue-600">
                                  <Star className="h-3 w-3 mr-1" />
                                  {test.score}/100
                                </Badge>
                              </div>
                            </div>
                          </div>

                          <div className="space-y-3">
                            <div className="bg-blue-50 p-3 rounded-lg">
                              <h5 className="font-medium text-blue-800 mb-2">Observaciones</h5>
                              <p className="text-blue-700 text-sm">{test.notes}</p>
                            </div>

                            <div className="bg-green-50 p-3 rounded-lg">
                              <h5 className="font-medium text-green-800 mb-2">Recomendaciones</h5>
                              <p className="text-green-700 text-sm">{test.recommendations}</p>
                            </div>
                          </div>
                        </CardContent>
                      </Card>
                    ))}
                  </div>
                </TabsContent>

                {/* Shelter Arrivals Tab */}
                <TabsContent value="arrivals" className="space-y-6">
                  <div className="text-center mb-6">
                    <h3 className="text-2xl font-bold text-blue-800 mb-2">Llegadas al Refugio</h3>
                    <p className="text-blue-700">Evaluaciones iniciales y tratamientos de ingreso</p>
                  </div>

                  <div className="space-y-4">
                    {vet.shelterArrivals.map((arrival) => (
                      <Card key={arrival.id} className="border-blue-200 hover:shadow-lg transition-all duration-300">
                        <CardContent className="p-6">
                          <div className="flex items-start justify-between mb-4">
                            <div>
                              <h4 className="text-lg font-semibold text-blue-800">{arrival.petName}</h4>
                              <p className="text-blue-600">Llegada: {arrival.arrivalDate.toLocaleDateString()}</p>
                              <p className="text-gray-600">
                                Motivo:{" "}
                                {arrival.reason === "ABANDONMENT"
                                  ? "Abandono"
                                  : arrival.reason === "SURRENDER"
                                    ? "Entrega"
                                    : "Rescate"}
                              </p>
                            </div>
                            <Badge
                              className={
                                arrival.condition === "GOOD"
                                  ? "bg-green-600"
                                  : arrival.condition === "FAIR"
                                    ? "bg-yellow-600"
                                    : "bg-red-600"
                              }
                            >
                              {arrival.condition === "GOOD"
                                ? "Buen Estado"
                                : arrival.condition === "FAIR"
                                  ? "Estado Regular"
                                  : "Estado Crítico"}
                            </Badge>
                          </div>

                          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                            <div>
                              <h5 className="font-medium text-gray-800 mb-2">Diagnóstico Inicial</h5>
                              <p className="text-gray-600 text-sm">{arrival.initialDiagnosis}</p>
                            </div>
                            <div>
                              <h5 className="font-medium text-gray-800 mb-2">Tratamiento</h5>
                              <p className="text-gray-600 text-sm">{arrival.treatment}</p>
                            </div>
                          </div>

                          <div className="bg-orange-50 p-3 rounded-lg">
                            <h5 className="font-medium text-orange-800 mb-2">Notas Adicionales</h5>
                            <p className="text-orange-700 text-sm">{arrival.notes}</p>
                          </div>
                        </CardContent>
                      </Card>
                    ))}
                  </div>
                </TabsContent>
              </Tabs>
            </div>
          </div>
        </div>
      </section>

      {/* Appointment Dialog */}
      <Dialog open={showAppointmentDialog} onOpenChange={setShowAppointmentDialog}>
        <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
          <DialogHeader>
            <DialogTitle className="text-blue-800 flex items-center text-xl">
              <Calendar className="h-6 w-6 mr-2" />
              Agendar Cita con {vet.name}
            </DialogTitle>
            <DialogDescription className="text-blue-600">
              Programa una consulta veterinaria. Selecciona fecha, hora y motivo de la cita.
            </DialogDescription>
          </DialogHeader>

          <form onSubmit={handleAppointmentSubmit} className="space-y-6">
            {/* Veterinario Info */}
            <div className="bg-blue-50 p-4 rounded-lg">
              <div className="flex items-center space-x-4">
                <Image
                  src={vet.image || "/placeholder.svg"}
                  alt={vet.name}
                  width={60}
                  height={60}
                  className="w-15 h-15 rounded-full object-cover"
                />
                <div>
                  <h4 className="font-semibold text-blue-800">{vet.name}</h4>
                  <p className="text-blue-600">{specialityLabels[vet.speciality as keyof typeof specialityLabels]}</p>
                  <p className="text-sm text-gray-600">{vet.shelter.name}</p>
                </div>
              </div>
            </div>

            {/* Date Selection */}
            <div className="space-y-2">
              <Label htmlFor="appointmentDate" className="text-blue-800 font-medium">
                Fecha de la Cita *
              </Label>
              <Input
                id="appointmentDate"
                type="date"
                required
                value={appointmentForm.date}
                onChange={(e) => setAppointmentForm((prev) => ({ ...prev, date: e.target.value }))}
                min={new Date().toISOString().split("T")[0]}
                className="border-blue-200 focus:border-blue-500"
              />
              <p className="text-xs text-gray-500">
                Disponible:{" "}
                {vet.disponibilities.map((d) => disponibilityLabels[d as keyof typeof disponibilityLabels]).join(", ")}
              </p>
            </div>

            {/* Time Selection */}
            <div className="space-y-2">
              <Label htmlFor="appointmentTime" className="text-blue-800 font-medium">
                Hora de la Cita *
              </Label>
              <Select
                value={appointmentForm.time}
                onValueChange={(value) => setAppointmentForm((prev) => ({ ...prev, time: value }))}
              >
                <SelectTrigger className="border-blue-200 focus:border-blue-500">
                  <SelectValue placeholder="Selecciona una hora" />
                </SelectTrigger>
                <SelectContent>
                  {getAvailableTimeSlots(vetId).map((time) => (
                    <SelectItem key={time} value={time}>
                      {time}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            {/* Pet Selection */}
            <div className="space-y-2">
              <Label htmlFor="petSelection" className="text-blue-800 font-medium">
                Mascota (Opcional)
              </Label>
              <Select
                value={appointmentForm.petId}
                onValueChange={(value) => setAppointmentForm((prev) => ({ ...prev, petId: value }))}
              >
                <SelectTrigger className="border-blue-200 focus:border-blue-500">
                  <SelectValue placeholder="Selecciona una mascota" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="none">Sin mascota específica</SelectItem>
                  {shelter.pets.map((pet) => (
                    <SelectItem key={pet.id} value={pet.id.toString()}>
                      {pet.name} - {pet.breed}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            {/* Reason Selection */}
            <div className="space-y-2">
              <Label htmlFor="appointmentReason" className="text-blue-800 font-medium">
                Motivo de la Consulta *
              </Label>
              <Select
                value={appointmentForm.reason}
                onValueChange={(value) => setAppointmentForm((prev) => ({ ...prev, reason: value }))}
              >
                <SelectTrigger className="border-blue-200 focus:border-blue-500">
                  <SelectValue placeholder="Selecciona el motivo" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="checkup">Revisión General</SelectItem>
                  <SelectItem value="vaccination">Vacunación</SelectItem>
                  <SelectItem value="emergency">Emergencia</SelectItem>
                  <SelectItem value="surgery">Cirugía</SelectItem>
                  <SelectItem value="followup">Seguimiento</SelectItem>
                  <SelectItem value="consultation">Consulta Especializada</SelectItem>
                  <SelectItem value="adoption">Evaluación para Adopción</SelectItem>
                  <SelectItem value="other">Otro</SelectItem>
                </SelectContent>
              </Select>
            </div>

            {/* Additional Notes */}
            <div className="space-y-2">
              <Label htmlFor="appointmentNotes" className="text-blue-800 font-medium">
                Notas Adicionales
              </Label>
              <Textarea
                id="appointmentNotes"
                value={appointmentForm.notes}
                onChange={(e) => setAppointmentForm((prev) => ({ ...prev, notes: e.target.value }))}
                className="border-blue-200 focus:border-blue-500"
                placeholder="Describe síntomas, comportamientos o información relevante..."
                rows={3}
              />
            </div>

            {/* Appointment Summary */}
            {appointmentForm.date && appointmentForm.time && appointmentForm.reason && (
              <div className="bg-green-50 p-4 rounded-lg border border-green-200">
                <h4 className="font-medium text-green-800 mb-2 flex items-center">
                  <CheckCircle className="h-4 w-4 mr-2" />
                  Resumen de la Cita
                </h4>
                <div className="space-y-1 text-sm text-green-700">
                  <p>
                    <strong>Veterinario:</strong> {vet.name}
                  </p>
                  <p>
                    <strong>Fecha:</strong>{" "}
                    {new Date(appointmentForm.date).toLocaleDateString("es-ES", {
                      weekday: "long",
                      year: "numeric",
                      month: "long",
                      day: "numeric",
                    })}
                  </p>
                  <p>
                    <strong>Hora:</strong> {appointmentForm.time}
                  </p>
                  <p>
                    <strong>Motivo:</strong> {appointmentForm.reason}
                  </p>
                  {appointmentForm.petId !== "none" && (
                    <p>
                      <strong>Mascota:</strong>{" "}
                      {shelter.pets.find((p) => p.id.toString() === appointmentForm.petId)?.name}
                    </p>
                  )}
                </div>
              </div>
            )}

            {/* Action Buttons */}
            <div className="flex gap-3 pt-4">
              <Button
                type="button"
                onClick={() => setShowAppointmentDialog(false)}
                variant="outline"
                className="flex-1 border-blue-600 text-blue-600 hover:bg-blue-50"
              >
                Cancelar
              </Button>
              <Button
                type="submit"
                disabled={!appointmentForm.date || !appointmentForm.time || !appointmentForm.reason}
                className="flex-1 bg-blue-600 hover:bg-blue-700 text-white"
              >
                <Calendar className="mr-2 h-4 w-4" />
                Confirmar Cita
              </Button>
            </div>
          </form>
        </DialogContent>
      </Dialog>

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
