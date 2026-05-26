"use client"

import type React from "react"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Badge } from "@/components/ui/badge"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import {
  ArrowLeft,
  Heart,
  Calendar,
  MapPin,
  Phone,
  Mail,
  PawPrint,
  Shield,
  Stethoscope,
  Camera,
  FileText,
  Home,
  AlertCircle,
  CheckCircle,
  Clock,
  Activity,
  Syringe,
  Bug,
  CalendarCheck,
  AlertTriangle,
  UserCheck,
  XCircle,
  RefreshCw,
  Award,
  Send,
} from "lucide-react"
import Image from "next/image"
import Link from "next/link"
import { useParams, useRouter } from "next/navigation"

// Mock data for pet details with enhanced adoption system
const petDetails = {
  1: {
    // Basic PetDTO info
    id: 1,
    name: "Luna",
    birthDate: new Date("2022-03-15"),
    breed: "Labrador Mix",
    size: "MEDIUM",
    gender: "FEMALE",
    behaviorProfile:
      "Amigable, juguetona y muy cariñosa. Le encanta correr y jugar con otros perros. Es perfecta para familias con niños y se adapta bien a diferentes ambientes.",
    shelter: {
      id: 1,
      name: "Refugio Esperanza",
      address: "Calle 45 #12-34, Bogotá",
      phone: "+57 301 234 5678",
      email: "contacto@refugioesperanza.org",
    },

    // Enhanced Vaccine Card Information
    vaccineCard: {
      id: 1,
      issuedDate: new Date("2023-08-15"),
      lastVaccineDate: new Date("2024-01-15"),
      lastDewormingDate: new Date("2024-01-10"),
      veterinarian: "Dr. María González",

      vaccines: [
        {
          id: 1,
          name: "Antirrábica",
          brandName: "Nobivac Rabies",
          date: new Date("2024-01-15"),
          nextDate: new Date("2025-01-15"),
          dosis: 1.0,
          status: "CURRENT",
        },
        {
          id: 2,
          name: "Múltiple (DHPP)",
          brandName: "Nobivac DHPPi",
          date: new Date("2024-01-10"),
          nextDate: new Date("2025-01-10"),
          dosis: 1.0,
          status: "CURRENT",
        },
        {
          id: 3,
          name: "Parvovirus",
          brandName: "Parvo-C",
          date: new Date("2023-12-20"),
          nextDate: new Date("2024-12-20"),
          dosis: 1.0,
          status: "DUE_SOON",
        },
      ],

      dewormings: [
        {
          id: 1,
          brandName: "Drontal Plus",
          date: new Date("2024-01-10"),
          nextDate: new Date("2024-04-10"),
          dosis: 2.0,
          type: "INTERNAL",
          veterinarian: "Dr. María González",
          status: "CURRENT",
          notes: "Desparasitación interna completa",
        },
        {
          id: 2,
          brandName: "Frontline Plus",
          date: new Date("2024-01-05"),
          nextDate: new Date("2024-02-05"),
          dosis: 1.0,
          type: "EXTERNAL",
          veterinarian: "Dr. Carlos Rodríguez",
          status: "DUE_SOON",
          notes: "Protección contra pulgas y garrapatas",
        },
      ],
    },

    // Enhanced Adoption System
    adoptionApplications: [
      {
        id: 1,
        applicationDate: new Date("2024-01-15"),
        applicationEnd: new Date("2024-01-20"),
        observations: "Familia con experiencia previa en perros grandes. Casa con jardín amplio.",
        applicationStatus: "APPROVED",
        result: "APPROVED",
        applicant: {
          id: 1,
          name: "María García",
          email: "maria.garcia@email.com",
          phone: "+57 300 111 2222",
          houseType: "HOUSE",
          address: "Carrera 15 #23-45, Bogotá",
          hasExperience: true,
          motivation: "Busco una compañera para mi familia, tenemos experiencia con perros grandes.",
        },
        veterinarian: {
          id: 1,
          name: "Dr. María González",
        },
        evaluationNotes: "Excelente candidata. Familia responsable con experiencia previa.",
        homeVisitDate: new Date("2024-01-18"),
        homeVisitResult: "APPROVED",
      },
      {
        id: 2,
        applicationDate: new Date("2024-01-10"),
        applicationEnd: null,
        observations: "Primera mascota. Apartamento pequeño pero con disponibilidad de tiempo.",
        applicationStatus: "PENDING",
        result: null,
        applicant: {
          id: 2,
          name: "Juan Rodríguez",
          email: "juan.rodriguez@email.com",
          phone: "+57 300 333 4444",
          houseType: "APARTMENT",
          address: "Calle 67 #89-12, Bogotá",
          hasExperience: false,
          motivation: "Quiero darle amor y cuidado a una mascota rescatada.",
        },
        veterinarian: {
          id: 1,
          name: "Dr. María González",
        },
        evaluationNotes: "Pendiente visita domiciliaria. Candidato sin experiencia pero motivado.",
        homeVisitDate: new Date("2024-02-05"),
        homeVisitResult: null,
      },
      {
        id: 3,
        applicationDate: new Date("2024-01-05"),
        applicationEnd: new Date("2024-01-12"),
        observations: "Familia joven con niños pequeños.",
        applicationStatus: "REJECTED",
        result: "REJECTED",
        applicant: {
          id: 3,
          name: "Carlos López",
          email: "carlos.lopez@email.com",
          phone: "+57 300 555 6666",
          houseType: "APARTMENT",
          address: "Avenida 45 #67-89, Bogotá",
          hasExperience: false,
          motivation: "Queremos una mascota para los niños.",
        },
        veterinarian: {
          id: 2,
          name: "Dr. Carlos Rodríguez",
        },
        evaluationNotes: "No cumple con los requisitos mínimos de espacio y experiencia.",
        homeVisitDate: null,
        homeVisitResult: "REJECTED",
      },
    ],

    // Current Adoption Status
    currentAdoption: null, // Available for adoption

    // Previous Adoptions
    adoptionHistory: [
      {
        id: 1,
        adoptionDate: new Date("2023-06-15"),
        adoptionEnd: new Date("2023-12-20"),
        observations: "Adopción exitosa inicial, devuelta por mudanza internacional.",
        adoptionStatus: "RETURN",
        owner: {
          id: 1,
          name: "Carlos Mendoza",
          email: "carlos.mendoza@email.com",
          phone: "+57 300 111 2222",
        },
        veterinarian: {
          id: 1,
          name: "Dr. María González",
        },
        returnReason: "Mudanza internacional - imposibilidad de llevar la mascota",
        followUps: [
          {
            date: new Date("2023-07-15"),
            notes: "Adaptación excelente, mascota muy feliz",
            status: "EXCELLENT",
          },
          {
            date: new Date("2023-09-15"),
            notes: "Continúa adaptación positiva, buena salud",
            status: "GOOD",
          },
        ],
      },
    ],

    medicalEvents: [
      {
        id: 1,
        date: new Date("2024-01-20"),
        type: "CHECKUP",
        description: "Revisión general de salud",
        veterinarian: "Dr. María González",
        diagnosis: "Excelente estado de salud",
        treatment: "Continuar con rutina de ejercicio y alimentación",
        nextAppointment: new Date("2024-04-20"),
      },
      {
        id: 2,
        date: new Date("2023-12-15"),
        type: "VACCINATION",
        description: "Refuerzo de vacunas anuales",
        veterinarian: "Dr. Carlos Rodríguez",
        diagnosis: "Vacunación completa",
        treatment: "Observación por 24 horas post-vacunación",
      },
    ],

    multimedia: [
      {
        id: 1,
        type: "IMAGE",
        url: "/placeholder.svg?height=400&width=600",
        description: "Luna jugando en el parque",
        uploadDate: new Date("2024-01-15"),
      },
      {
        id: 2,
        type: "IMAGE",
        url: "/placeholder.svg?height=400&width=600",
        description: "Luna con su juguete favorito",
        uploadDate: new Date("2024-01-10"),
      },
    ],

    shelterArrival: {
      id: 1,
      arrivalDate: new Date("2023-08-10"),
      reason: "RETURN",
      condition: "GOOD",
      rescuer: "Dueño anterior - Carlos Mendoza",
      notes: "Devuelta por mudanza internacional. Excelente estado de salud y comportamiento.",
      initialWeight: 20.5,
      currentWeight: 22.3,
    },

    // Additional display info
    images: [
      "/placeholder.svg?height=500&width=700",
      "/placeholder.svg?height=500&width=700",
      "/placeholder.svg?height=500&width=700",
      "/placeholder.svg?height=500&width=700",
    ],
    isAvailable: true,
    adoptionFee: 150000,
  },
}

// Helper functions
function calculateAge(birthDate: Date): string {
  const today = new Date()
  const age = today.getFullYear() - birthDate.getFullYear()
  const monthDiff = today.getMonth() - birthDate.getMonth()

  if (age === 0 || (age === 1 && monthDiff < 0)) {
    const months = monthDiff < 0 ? 12 + monthDiff : monthDiff
    return `${months} meses`
  }

  return `${age} años`
}

function getSizeLabel(size: string): string {
  const sizeMap: { [key: string]: string } = {
    SMALL: "Pequeño",
    MEDIUM: "Mediano",
    LARGE: "Grande",
  }
  return sizeMap[size] || size
}

function getGenderLabel(gender: string): string {
  return gender === "MALE" ? "Macho" : "Hembra"
}

function getApplicationStatusBadge(status: string) {
  const statusConfig = {
    PENDING: { label: "Pendiente", color: "bg-yellow-600", icon: Clock },
    APPROVED: { label: "Aprobada", color: "bg-green-600", icon: CheckCircle },
    REJECTED: { label: "Rechazada", color: "bg-red-600", icon: XCircle },
    CANCELED: { label: "Cancelada", color: "bg-gray-600", icon: AlertCircle },
  }

  const config = statusConfig[status as keyof typeof statusConfig] || statusConfig.PENDING
  const Icon = config.icon

  return (
    <Badge className={`${config.color} text-white`}>
      <Icon className="h-3 w-3 mr-1" />
      {config.label}
    </Badge>
  )
}

function getAdoptionStatusBadge(status: string) {
  const statusConfig = {
    DONE: { label: "Completada", color: "bg-green-600", icon: CheckCircle },
    RETURN: { label: "Devuelta", color: "bg-yellow-600", icon: RefreshCw },
    DECEASED: { label: "Fallecida", color: "bg-gray-600", icon: XCircle },
    CANCELED: { label: "Cancelada", color: "bg-red-600", icon: XCircle },
    PENDING: { label: "Pendiente", color: "bg-blue-600", icon: Clock },
  }

  const config = statusConfig[status as keyof typeof statusConfig] || statusConfig.PENDING
  const Icon = config.icon

  return (
    <Badge className={`${config.color} text-white`}>
      <Icon className="h-3 w-3 mr-1" />
      {config.label}
    </Badge>
  )
}

function getVaccineStatusBadge(status: string) {
  const statusConfig = {
    CURRENT: { label: "Al día", color: "bg-green-600", icon: CheckCircle },
    DUE_SOON: { label: "Próxima", color: "bg-yellow-600", icon: Clock },
    OVERDUE: { label: "Vencida", color: "bg-red-600", icon: AlertTriangle },
  }

  const config = statusConfig[status as keyof typeof statusConfig] || statusConfig.CURRENT
  const Icon = config.icon

  return (
    <Badge className={`${config.color} text-white`}>
      <Icon className="h-3 w-3 mr-1" />
      {config.label}
    </Badge>
  )
}

function getDewormingTypeLabel(type: string): string {
  return type === "INTERNAL" ? "Interna" : "Externa"
}

function getDewormingTypeBadge(type: string) {
  const isInternal = type === "INTERNAL"
  return (
    <Badge className={isInternal ? "bg-blue-600 text-white" : "bg-purple-600 text-white"}>
      <Bug className="h-3 w-3 mr-1" />
      {getDewormingTypeLabel(type)}
    </Badge>
  )
}

export default function PetDetailPage() {
  const params = useParams()
  const router = useRouter()
  const petId = Number.parseInt(params.id as string)
  const pet = petDetails[petId as keyof typeof petDetails]
  const [activeTab, setActiveTab] = useState("info")
  const [selectedImage, setSelectedImage] = useState(0)
  const [showApplicationDialog, setShowApplicationDialog] = useState(false)
  const [applicationForm, setApplicationForm] = useState({
    motivation: "",
    hasExperience: false,
    currentPets: "",
    livingSpace: "",
    familyMembers: "",
    workSchedule: "",
    emergencyContact: "",
    veterinarianContact: "",
    additionalNotes: "",
  })

  const [showMultimediaDialog, setShowMultimediaDialog] = useState(false)
  const [multimediaForm, setMultimediaForm] = useState({
    url: "",
    description: "",
    multimediaType: "IMAGE" as "IMAGE" | "VIDEO",
    file: null as File | null,
  })

  if (!pet) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-orange-50 to-amber-50 flex items-center justify-center">
        <div className="text-center">
          <AlertCircle className="h-16 w-16 text-orange-600 mx-auto mb-4" />
          <h2 className="text-2xl font-bold text-orange-800 mb-2">Mascota no encontrada</h2>
          <p className="text-orange-700 mb-4">La mascota que buscas no existe o ha sido adoptada.</p>
          <Button asChild>
            <Link href="/">
              <ArrowLeft className="h-4 w-4 mr-2" />
              Volver al Inicio
            </Link>
          </Button>
        </div>
      </div>
    )
  }

  const handleApplicationSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    // Simulate API call
    await new Promise((resolve) => setTimeout(resolve, 2000))

    // Here you would submit the adoption application
    console.log("Adoption application submitted:", applicationForm)

    setShowApplicationDialog(false)
    setApplicationForm({
      motivation: "",
      hasExperience: false,
      currentPets: "",
      livingSpace: "",
      familyMembers: "",
      workSchedule: "",
      emergencyContact: "",
      veterinarianContact: "",
      additionalNotes: "",
    })

    // Show success message and redirect to application tracking
    alert("¡Solicitud de adopción enviada exitosamente! Te contactaremos pronto para programar la visita domiciliaria.")
    router.push("/adoption/dashboard")
  }

  const handleAdoptionClick = () => {
    const user = localStorage.getItem("adoptionUser")
    if (!user) {
      router.push("/register")
    } else {
      setShowApplicationDialog(true)
    }
  }

  const handleMultimediaUpload = async (e: React.FormEvent) => {
    e.preventDefault()

    // Simulate file upload
    await new Promise((resolve) => setTimeout(resolve, 2000))

    // Create new multimedia entry
    const newMultimedia = {
      id: pet.multimedia.length + 1,
      type: multimediaForm.multimediaType,
      url: multimediaForm.url || "/placeholder.svg?height=400&width=600",
      description: multimediaForm.description,
      uploadDate: new Date(),
    }

    // Add to pet multimedia (in real app, this would be an API call)
    pet.multimedia.push(newMultimedia)

    setShowMultimediaDialog(false)
    setMultimediaForm({
      url: "",
      description: "",
      multimediaType: "IMAGE",
      file: null,
    })

    alert("¡Multimedia subida exitosamente!")
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
              onClick={handleApplicationSubmit}
              className="flex-2 bg-gradient-to-r from-orange-600 to-red-600 hover:from-orange-700 hover:to-red-700 text-white shadow-lg hover:shadow-xl transition-all duration-300"
            >
              <Send className="mr-2 h-4 w-4" />
              Enviar Solicitud de Adopción
            </Button>
          </div>
        </div>
      </header>

      {/* Pet Images Gallery */}
      <section className="py-8">
        <div className="container mx-auto px-4">
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
            {/* Main Image */}
            <div className="lg:col-span-2">
              <div className="relative aspect-video rounded-xl overflow-hidden shadow-xl">
                <Image
                  src={pet.images[selectedImage] || "/placeholder.svg"}
                  alt={pet.name}
                  width={700}
                  height={500}
                  className="w-full h-full object-cover"
                />
                {pet.isAvailable && (
                  <Badge className="absolute top-4 right-4 bg-green-600 text-white shadow-lg">
                    <Heart className="h-3 w-3 mr-1" />
                    Disponible
                  </Badge>
                )}
              </div>

              {/* Thumbnail Gallery */}
              <div className="flex gap-2 mt-4 overflow-x-auto">
                {pet.images.map((image, index) => (
                  <button
                    key={index}
                    onClick={() => setSelectedImage(index)}
                    className={`flex-shrink-0 w-20 h-20 rounded-lg overflow-hidden border-2 transition-all ${
                      selectedImage === index ? "border-orange-500" : "border-gray-200 hover:border-orange-300"
                    }`}
                  >
                    <Image
                      src={image || "/placeholder.svg"}
                      alt={`${pet.name} ${index + 1}`}
                      width={80}
                      height={80}
                      className="w-full h-full object-cover"
                    />
                  </button>
                ))}
              </div>
            </div>

            {/* Pet Basic Info */}
            <div className="space-y-6">
              <Card className="border-orange-200 shadow-lg">
                <CardHeader className="bg-gradient-to-r from-orange-50 to-amber-50">
                  <CardTitle className="text-2xl text-orange-800">{pet.name}</CardTitle>
                  <CardDescription className="text-orange-600 text-lg">
                    {pet.breed} • {getSizeLabel(pet.size)} • {getGenderLabel(pet.gender)}
                  </CardDescription>
                </CardHeader>
                <CardContent className="p-6 space-y-4">
                  <div className="grid grid-cols-2 gap-4">
                    <div className="flex items-center text-sm">
                      <Calendar className="h-4 w-4 mr-2 text-orange-600" />
                      <span className="font-medium">Edad:</span>
                      <span className="ml-2">{calculateAge(pet.birthDate)}</span>
                    </div>
                    <div className="flex items-center text-sm">
                      <Shield className="h-4 w-4 mr-2 text-green-600" />
                      <span className="font-medium">Vacunado</span>
                    </div>
                  </div>

                  <div className="bg-orange-50 p-4 rounded-lg">
                    <h4 className="font-semibold text-orange-800 mb-2">Personalidad</h4>
                    <p className="text-orange-700 text-sm">{pet.behaviorProfile}</p>
                  </div>

                  <div className="border-t pt-4">
                    <h4 className="font-semibold text-orange-800 mb-3">Refugio</h4>
                    <div className="space-y-2">
                      <div className="flex items-center text-sm text-gray-600">
                        <MapPin className="h-4 w-4 mr-2 text-orange-600" />
                        <Link href={`/shelter/${pet.shelter.id}`} className="hover:text-orange-600 transition-colors">
                          {pet.shelter.name}
                        </Link>
                      </div>
                      <div className="flex items-center text-sm text-gray-600">
                        <Phone className="h-4 w-4 mr-2 text-orange-600" />
                        {pet.shelter.phone}
                      </div>
                      <div className="flex items-center text-sm text-gray-600">
                        <Mail className="h-4 w-4 mr-2 text-orange-600" />
                        {pet.shelter.email}
                      </div>
                    </div>
                  </div>

                  {pet.adoptionFee && (
                    <div className="bg-green-50 p-4 rounded-lg border border-green-200">
                      <div className="flex items-center justify-between">
                        <span className="font-semibold text-green-800">Aporte de Adopción</span>
                        <span className="text-xl font-bold text-green-600">${pet.adoptionFee.toLocaleString()}</span>
                      </div>
                      <p className="text-xs text-green-600 mt-1">Incluye vacunas, esterilización y microchip</p>
                    </div>
                  )}
                </CardContent>
              </Card>
            </div>
          </div>
        </div>
      </section>

      {/* Detailed Information Tabs */}
      <section className="py-8 px-4">
        <div className="container mx-auto">
          <Tabs value={activeTab} onValueChange={setActiveTab} className="w-full">
            <TabsList className="grid w-full grid-cols-6 max-w-4xl mx-auto mb-8 bg-orange-100/80 backdrop-blur-sm shadow-lg">
              <TabsTrigger
                value="info"
                className="data-[state=active]:bg-orange-600 data-[state=active]:text-white transition-all duration-300 flex items-center text-sm"
              >
                <FileText className="h-4 w-4 mr-1" />
                Info
              </TabsTrigger>
              <TabsTrigger
                value="vaccines"
                className="data-[state=active]:bg-orange-600 data-[state=active]:text-white transition-all duration-300 flex items-center text-sm"
              >
                <Syringe className="h-4 w-4 mr-1" />
                Carnet de Vacunación
              </TabsTrigger>
              <TabsTrigger
                value="medical"
                className="data-[state=active]:bg-orange-600 data-[state=active]:text-white transition-all duration-300 flex items-center text-sm"
              >
                <Stethoscope className="h-4 w-4 mr-1" />
                Médico
              </TabsTrigger>
              <TabsTrigger
                value="applications"
                className="data-[state=active]:bg-orange-600 data-[state=active]:text-white transition-all duration-300 flex items-center text-sm"
              >
                <UserCheck className="h-4 w-4 mr-1" />
                Solicitudes
              </TabsTrigger>
              <TabsTrigger
                value="adoptions"
                className="data-[state=active]:bg-orange-600 data-[state=active]:text-white transition-all duration-300 flex items-center text-sm"
              >
                <Award className="h-4 w-4 mr-1" />
                Adopciones
              </TabsTrigger>
              <TabsTrigger
                value="media"
                className="data-[state=active]:bg-orange-600 data-[state=active]:text-white transition-all duration-300 flex items-center text-sm"
              >
                <Camera className="h-4 w-4 mr-1" />
                Fotos
              </TabsTrigger>
            </TabsList>

            {/* General Info Tab */}
            <TabsContent value="info" className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <Card className="border-orange-200">
                  <CardHeader>
                    <CardTitle className="text-orange-800 flex items-center">
                      <Shield className="h-5 w-5 mr-2" />
                      Resumen de Salud
                    </CardTitle>
                  </CardHeader>
                  <CardContent className="space-y-4">
                    <div className="bg-green-50 p-3 rounded-lg">
                      <div className="flex items-center justify-between mb-2">
                        <span className="font-medium text-green-800">Estado de Vacunación</span>
                        <Badge className="bg-green-600 text-white">Completa</Badge>
                      </div>
                      <p className="text-sm text-green-600">
                        Última vacuna: {pet.vaccineCard.lastVaccineDate.toLocaleDateString()}
                      </p>
                      <p className="text-sm text-green-600">Veterinario: {pet.vaccineCard.veterinarian}</p>
                    </div>

                    <div className="bg-blue-50 p-3 rounded-lg">
                      <div className="flex items-center justify-between mb-2">
                        <span className="font-medium text-blue-800">Desparasitación</span>
                        <Badge className="bg-blue-600 text-white">Al día</Badge>
                      </div>
                      <p className="text-sm text-blue-600">
                        Última desparasitación: {pet.vaccineCard.lastDewormingDate.toLocaleDateString()}
                      </p>
                    </div>
                  </CardContent>
                </Card>

                <Card className="border-orange-200">
                  <CardHeader>
                    <CardTitle className="text-orange-800 flex items-center">
                      <Home className="h-5 w-5 mr-2" />
                      Llegada al Refugio
                    </CardTitle>
                  </CardHeader>
                  <CardContent className="space-y-4">
                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <span className="text-sm font-medium text-gray-600">Fecha de llegada</span>
                        <p className="text-orange-800">{pet.shelterArrival.arrivalDate.toLocaleDateString()}</p>
                      </div>
                      <div>
                        <span className="text-sm font-medium text-gray-600">Motivo</span>
                        <p className="text-orange-800">
                          {pet.shelterArrival.reason === "ABANDONMENT"
                            ? "Abandono"
                            : pet.shelterArrival.reason === "SURRENDER"
                              ? "Entrega"
                              : pet.shelterArrival.reason === "RETURN"
                                ? "Devolución"
                                : "Rescate"}
                        </p>
                      </div>
                      <div>
                        <span className="text-sm font-medium text-gray-600">Peso inicial</span>
                        <p className="text-orange-800">{pet.shelterArrival.initialWeight} kg</p>
                      </div>
                      <div>
                        <span className="text-sm font-medium text-gray-600">Peso actual</span>
                        <p className="text-orange-800">{pet.shelterArrival.currentWeight} kg</p>
                      </div>
                    </div>

                    <div className="bg-orange-50 p-3 rounded-lg">
                      <h4 className="font-medium text-orange-800 mb-2">Notas del rescate</h4>
                      <p className="text-sm text-orange-700">{pet.shelterArrival.notes}</p>
                    </div>
                  </CardContent>
                </Card>
              </div>
            </TabsContent>

            {/* Vaccines Tab */}
            <TabsContent value="vaccines" className="space-y-6">
              <div className="text-center mb-6">
                <h3 className="text-2xl font-bold text-orange-800 mb-2">Carnet de Vacunación</h3>
                <p className="text-orange-700">Historial completo de vacunas y desparasitaciones de {pet.name}</p>
              </div>

              <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                {/* Vaccines Section */}
                <Card className="border-green-200">
                  <CardHeader className="bg-gradient-to-r from-green-50 to-emerald-50">
                    <CardTitle className="text-green-800 flex items-center">
                      <Syringe className="h-5 w-5 mr-2" />
                      Vacunas
                    </CardTitle>
                    <CardDescription className="text-green-600">Registro de vacunación completo</CardDescription>
                  </CardHeader>
                  <CardContent className="p-6 space-y-4">
                    {pet.vaccineCard.vaccines.map((vaccine) => (
                      <div key={vaccine.id} className="border border-green-200 p-4 rounded-lg">
                        <div className="flex justify-between items-start mb-3">
                          <div>
                            <h4 className="font-semibold text-green-800">{vaccine.name}</h4>
                            <p className="text-sm text-gray-600">{vaccine.brandName}</p>
                          </div>
                          {getVaccineStatusBadge(vaccine.status)}
                        </div>

                        <div className="grid grid-cols-2 gap-3 text-sm">
                          <div>
                            <span className="font-medium text-gray-700">Aplicada:</span>
                            <p className="text-green-700">{vaccine.date.toLocaleDateString()}</p>
                          </div>
                          <div>
                            <span className="font-medium text-gray-700">Próxima dosis:</span>
                            <p className="text-green-700">{vaccine.nextDate.toLocaleDateString()}</p>
                          </div>
                          <div>
                            <span className="font-medium text-gray-700">Dosis:</span>
                            <p className="text-green-700">{vaccine.dosis} ml</p>
                          </div>
                          <div className="flex items-center">
                            <CalendarCheck className="h-4 w-4 mr-1 text-green-600" />
                            <span className="text-xs text-green-600">
                              {Math.ceil((vaccine.nextDate.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24))}{" "}
                              días
                            </span>
                          </div>
                        </div>
                      </div>
                    ))}
                  </CardContent>
                </Card>

                {/* Dewormings Section */}
                <Card className="border-purple-200">
                  <CardHeader className="bg-gradient-to-r from-purple-50 to-violet-50">
                    <CardTitle className="text-purple-800 flex items-center">
                      <Bug className="h-5 w-5 mr-2" />
                      Desparasitaciones
                    </CardTitle>
                    <CardDescription className="text-purple-600">Tratamientos internos y externos</CardDescription>
                  </CardHeader>
                  <CardContent className="p-6 space-y-4">
                    {pet.vaccineCard.dewormings.map((deworming) => (
                      <div key={deworming.id} className="border border-purple-200 p-4 rounded-lg">
                        <div className="flex justify-between items-start mb-3">
                          <div>
                            <h4 className="font-semibold text-purple-800">{deworming.brandName}</h4>
                            <div className="flex items-center gap-2 mt-1">
                              {getDewormingTypeBadge(deworming.type)}
                              {getVaccineStatusBadge(deworming.status)}
                            </div>
                          </div>
                        </div>

                        <div className="grid grid-cols-2 gap-3 text-sm mb-3">
                          <div>
                            <span className="font-medium text-gray-700">Aplicada:</span>
                            <p className="text-purple-700">{deworming.date.toLocaleDateString()}</p>
                          </div>
                          <div>
                            <span className="font-medium text-gray-700">Próxima:</span>
                            <p className="text-purple-700">{deworming.nextDate.toLocaleDateString()}</p>
                          </div>
                          <div>
                            <span className="font-medium text-gray-700">Dosis:</span>
                            <p className="text-purple-700">{deworming.dosis} ml</p>
                          </div>
                          <div>
                            <span className="font-medium text-gray-700">Veterinario:</span>
                            <p className="text-purple-700 text-xs">{deworming.veterinarian}</p>
                          </div>
                        </div>

                        {deworming.notes && (
                          <div className="bg-purple-50 p-2 rounded text-xs">
                            <span className="font-medium text-purple-800">Notas: </span>
                            <span className="text-purple-700">{deworming.notes}</span>
                          </div>
                        )}
                      </div>
                    ))}
                  </CardContent>
                </Card>
              </div>
            </TabsContent>

            {/* Medical History Tab */}
            <TabsContent value="medical" className="space-y-6">
              <div className="text-center mb-6">
                <h3 className="text-2xl font-bold text-orange-800 mb-2">Historial Médico</h3>
                <p className="text-orange-700">Registro completo de eventos médicos y tratamientos</p>
              </div>

              <div className="space-y-4">
                {pet.medicalEvents.map((event) => (
                  <Card key={event.id} className="border-orange-200">
                    <CardContent className="p-6">
                      <div className="flex items-start justify-between mb-4">
                        <div>
                          <h3 className="text-lg font-semibold text-orange-800">{event.description}</h3>
                          <p className="text-orange-600">{event.date.toLocaleDateString()}</p>
                        </div>
                        <Badge
                          className={
                            event.type === "CHECKUP"
                              ? "bg-blue-600"
                              : event.type === "VACCINATION"
                                ? "bg-green-600"
                                : event.type === "SURGERY"
                                  ? "bg-red-600"
                                  : "bg-gray-600"
                          }
                        >
                          {event.type === "CHECKUP"
                            ? "Revisión"
                            : event.type === "VACCINATION"
                              ? "Vacunación"
                              : event.type === "SURGERY"
                                ? "Cirugía"
                                : event.type}
                        </Badge>
                      </div>

                      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                          <h4 className="font-medium text-gray-800 mb-2">Veterinario</h4>
                          <p className="text-gray-600">{event.veterinarian}</p>
                        </div>
                        <div>
                          <h4 className="font-medium text-gray-800 mb-2">Diagnóstico</h4>
                          <p className="text-gray-600">{event.diagnosis}</p>
                        </div>
                      </div>

                      <div className="mt-4">
                        <h4 className="font-medium text-gray-800 mb-2">Tratamiento</h4>
                        <p className="text-gray-600">{event.treatment}</p>
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
                <h3 className="text-2xl font-bold text-orange-800 mb-2">Solicitudes de Adopción</h3>
                <p className="text-orange-700">Personas interesadas en adoptar a {pet.name}</p>
              </div>

              <div className="space-y-4">
                {pet.adoptionApplications.map((application) => (
                  <Card key={application.id} className="border-orange-200 hover:shadow-lg transition-all duration-300">
                    <CardContent className="p-6">
                      <div className="flex items-start justify-between mb-4">
                        <div>
                          <h3 className="text-lg font-semibold text-orange-800">{application.applicant.name}</h3>
                          <p className="text-orange-600">{application.applicationDate.toLocaleDateString()}</p>
                          <p className="text-sm text-gray-600">Evaluado por: {application.veterinarian.name}</p>
                        </div>
                        {getApplicationStatusBadge(application.applicationStatus)}
                      </div>

                      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                        <div>
                          <span className="text-sm font-medium text-gray-600">Tipo de vivienda</span>
                          <p className="text-gray-800">
                            {application.applicant.houseType === "HOUSE" ? "Casa" : "Apartamento"}
                          </p>
                        </div>
                        <div>
                          <span className="text-sm font-medium text-gray-600">Experiencia con mascotas</span>
                          <p className="text-gray-800">{application.applicant.hasExperience ? "Sí" : "No"}</p>
                        </div>
                        <div>
                          <span className="text-sm font-medium text-gray-600">Teléfono</span>
                          <p className="text-gray-800">{application.applicant.phone}</p>
                        </div>
                        <div>
                          <span className="text-sm font-medium text-gray-600">Email</span>
                          <p className="text-gray-800">{application.applicant.email}</p>
                        </div>
                      </div>

                      <div className="space-y-3">
                        <div className="bg-gray-50 p-3 rounded-lg">
                          <h4 className="font-medium text-gray-800 mb-2">Motivación</h4>
                          <p className="text-gray-600 text-sm">{application.applicant.motivation}</p>
                        </div>

                        <div className="bg-blue-50 p-3 rounded-lg">
                          <h4 className="font-medium text-blue-800 mb-2">Evaluación Veterinaria</h4>
                          <p className="text-blue-700 text-sm">{application.evaluationNotes}</p>
                        </div>

                        {application.homeVisitDate && (
                          <div className="bg-green-50 p-3 rounded-lg">
                            <h4 className="font-medium text-green-800 mb-2">Visita Domiciliaria</h4>
                            <div className="flex items-center justify-between">
                              <p className="text-green-700 text-sm">
                                Fecha: {application.homeVisitDate.toLocaleDateString()}
                              </p>
                              {application.homeVisitResult && (
                                <Badge
                                  className={
                                    application.homeVisitResult === "APPROVED"
                                      ? "bg-green-600 text-white"
                                      : "bg-red-600 text-white"
                                  }
                                >
                                  {application.homeVisitResult === "APPROVED" ? "Aprobada" : "Rechazada"}
                                </Badge>
                              )}
                            </div>
                          </div>
                        )}

                        {application.applicationEnd && (
                          <div className="text-sm text-gray-500">
                            Proceso finalizado: {application.applicationEnd.toLocaleDateString()}
                          </div>
                        )}
                      </div>
                    </CardContent>
                  </Card>
                ))}
              </div>
            </TabsContent>

            {/* Adoptions History Tab */}
            <TabsContent value="adoptions" className="space-y-6">
              <div className="text-center mb-6">
                <h3 className="text-2xl font-bold text-orange-800 mb-2">Historial de Adopciones</h3>
                <p className="text-orange-700">Registro completo de adopciones de {pet.name}</p>
              </div>

              {pet.currentAdoption ? (
                <Card className="border-green-200 bg-green-50">
                  <CardHeader>
                    <CardTitle className="text-green-800 flex items-center">
                      <CheckCircle className="h-5 w-5 mr-2" />
                      Adopción Actual
                    </CardTitle>
                  </CardHeader>
                  <CardContent>
                    {/* Current adoption details would go here */}
                    <p className="text-green-700">Actualmente adoptado</p>
                  </CardContent>
                </Card>
              ) : (
                <Card className="border-blue-200 bg-blue-50">
                  <CardHeader>
                    <CardTitle className="text-blue-800 flex items-center">
                      <Heart className="h-5 w-5 mr-2" />
                      Estado Actual
                    </CardTitle>
                  </CardHeader>
                  <CardContent>
                    <p className="text-blue-700">Disponible para adopción</p>
                  </CardContent>
                </Card>
              )}

              {pet.adoptionHistory.length > 0 && (
                <div className="space-y-4">
                  <h4 className="text-xl font-semibold text-orange-800">Adopciones Anteriores</h4>
                  {pet.adoptionHistory.map((adoption) => (
                    <Card key={adoption.id} className="border-orange-200">
                      <CardContent className="p-6">
                        <div className="flex items-start justify-between mb-4">
                          <div>
                            <h3 className="text-lg font-semibold text-orange-800">{adoption.owner.name}</h3>
                            <p className="text-orange-600">
                              {adoption.adoptionDate.toLocaleDateString()} -{" "}
                              {adoption.adoptionEnd?.toLocaleDateString()}
                            </p>
                            <p className="text-sm text-gray-600">Veterinario: {adoption.veterinarian.name}</p>
                          </div>
                          {getAdoptionStatusBadge(adoption.adoptionStatus)}
                        </div>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                          <div>
                            <span className="text-sm font-medium text-gray-600">Teléfono</span>
                            <p className="text-gray-800">{adoption.owner.phone}</p>
                          </div>
                          <div>
                            <span className="text-sm font-medium text-gray-600">Email</span>
                            <p className="text-gray-800">{adoption.owner.email}</p>
                          </div>
                        </div>

                        <div className="space-y-3">
                          <div className="bg-gray-50 p-3 rounded-lg">
                            <h4 className="font-medium text-gray-800 mb-2">Observaciones</h4>
                            <p className="text-gray-600 text-sm">{adoption.observations}</p>
                          </div>

                          {adoption.returnReason && (
                            <div className="bg-yellow-50 p-3 rounded-lg">
                              <h4 className="font-medium text-yellow-800 mb-2">Motivo de Devolución</h4>
                              <p className="text-yellow-700 text-sm">{adoption.returnReason}</p>
                            </div>
                          )}

                          {adoption.followUps && adoption.followUps.length > 0 && (
                            <div className="bg-blue-50 p-3 rounded-lg">
                              <h4 className="font-medium text-blue-800 mb-2">Seguimientos</h4>
                              <div className="space-y-2">
                                {adoption.followUps.map((followUp, index) => (
                                  <div key={index} className="text-sm">
                                    <div className="flex items-center justify-between">
                                      <span className="font-medium text-blue-700">
                                        {followUp.date.toLocaleDateString()}
                                      </span>
                                      <Badge
                                        className={
                                          followUp.status === "EXCELLENT"
                                            ? "bg-green-600 text-white"
                                            : followUp.status === "GOOD"
                                              ? "bg-yellow-600 text-white"
                                              : "bg-red-600 text-white"
                                        }
                                      >
                                        {followUp.status === "EXCELLENT"
                                          ? "Excelente"
                                          : followUp.status === "GOOD"
                                            ? "Bueno"
                                            : "Regular"}
                                      </Badge>
                                    </div>
                                    <p className="text-blue-600 mt-1">{followUp.notes}</p>
                                  </div>
                                ))}
                              </div>
                            </div>
                          )}
                        </div>
                      </CardContent>
                    </Card>
                  ))}
                </div>
              )}
            </TabsContent>

            {/* Media Tab */}
            <TabsContent value="media" className="space-y-6">
              <div className="flex justify-between items-center mb-6">
                <div className="text-center flex-1">
                  <h3 className="text-2xl font-bold text-orange-800 mb-2">Galería de {pet.name}</h3>
                  <p className="text-orange-700">Fotos y videos de nuestra querida mascota</p>
                </div>
                <Button
                  onClick={() => setShowMultimediaDialog(true)}
                  className="bg-orange-600 hover:bg-orange-700 text-white"
                >
                  <Camera className="h-4 w-4 mr-2" />
                  Subir Multimedia
                </Button>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {pet.multimedia.map((media) => (
                  <Card key={media.id} className="border-orange-200 overflow-hidden">
                    <div className="relative aspect-video">
                      <Image
                        src={media.url || "/placeholder.svg"}
                        alt={media.description}
                        width={400}
                        height={300}
                        className="w-full h-full object-cover"
                      />
                      {media.type === "VIDEO" && (
                        <div className="absolute inset-0 bg-black/20 flex items-center justify-center">
                          <div className="bg-white/90 rounded-full p-3">
                            <Activity className="h-6 w-6 text-orange-600" />
                          </div>
                        </div>
                      )}
                    </div>
                    <CardContent className="p-4">
                      <h4 className="font-medium text-orange-800 mb-1">{media.description}</h4>
                      <p className="text-sm text-gray-600">{media.uploadDate.toLocaleDateString()}</p>
                    </CardContent>
                  </Card>
                ))}
              </div>
            </TabsContent>
          </Tabs>
        </div>
      </section>

      {/* Multimedia Upload Dialog */}
      <Dialog open={showMultimediaDialog} onOpenChange={setShowMultimediaDialog}>
        <DialogContent className="max-w-2xl">
          <DialogHeader>
            <DialogTitle className="text-orange-800 flex items-center text-xl">
              <Camera className="h-5 w-5 mr-2" />
              Subir Multimedia para {pet.name}
            </DialogTitle>
            <DialogDescription className="text-orange-600">
              Comparte fotos o videos de {pet.name} para ayudar en su proceso de adopción
            </DialogDescription>
          </DialogHeader>

          <form onSubmit={handleMultimediaUpload} className="space-y-6">
            {/* File Upload */}
            <div className="space-y-2">
              <label className="text-sm font-medium text-gray-700">Archivo</label>
              <div className="border-2 border-dashed border-orange-300 rounded-lg p-6 text-center hover:border-orange-400 transition-colors">
                <input
                  type="file"
                  accept="image/*,video/*"
                  onChange={(e) => {
                    const file = e.target.files?.[0]
                    if (file) {
                      setMultimediaForm((prev) => ({
                        ...prev,
                        file,
                        multimediaType: file.type.startsWith("video/") ? "VIDEO" : "IMAGE",
                      }))
                    }
                  }}
                  className="hidden"
                  id="multimedia-upload"
                />
                <label htmlFor="multimedia-upload" className="cursor-pointer">
                  <div className="space-y-2">
                    <Camera className="h-12 w-12 text-orange-400 mx-auto" />
                    <div>
                      <p className="text-orange-600 font-medium">Haz clic para subir archivo</p>
                      <p className="text-sm text-gray-500">PNG, JPG, MP4 hasta 10MB</p>
                    </div>
                  </div>
                </label>
                {multimediaForm.file && (
                  <div className="mt-4 p-3 bg-orange-50 rounded-lg">
                    <p className="text-sm text-orange-800">
                      <strong>Archivo seleccionado:</strong> {multimediaForm.file.name}
                    </p>
                    <p className="text-xs text-orange-600">
                      Tipo: {multimediaForm.multimediaType} • Tamaño:{" "}
                      {(multimediaForm.file.size / 1024 / 1024).toFixed(2)} MB
                    </p>
                  </div>
                )}
              </div>
            </div>

            {/* URL Alternative */}
            <div className="space-y-2">
              <label className="text-sm font-medium text-gray-700">O ingresa una URL</label>
              <input
                type="url"
                value={multimediaForm.url}
                onChange={(e) => setMultimediaForm((prev) => ({ ...prev, url: e.target.value }))}
                placeholder="https://ejemplo.com/imagen.jpg"
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500"
              />
            </div>

            {/* Type Selection */}
            <div className="space-y-2">
              <label className="text-sm font-medium text-gray-700">Tipo de Multimedia</label>
              <div className="flex gap-4">
                <label className="flex items-center">
                  <input
                    type="radio"
                    value="IMAGE"
                    checked={multimediaForm.multimediaType === "IMAGE"}
                    onChange={(e) =>
                      setMultimediaForm((prev) => ({ ...prev, multimediaType: e.target.value as "IMAGE" | "VIDEO" }))
                    }
                    className="mr-2"
                  />
                  <Camera className="h-4 w-4 mr-1" />
                  Imagen
                </label>
                <label className="flex items-center">
                  <input
                    type="radio"
                    value="VIDEO"
                    checked={multimediaForm.multimediaType === "VIDEO"}
                    onChange={(e) =>
                      setMultimediaForm((prev) => ({ ...prev, multimediaType: e.target.value as "IMAGE" | "VIDEO" }))
                    }
                    className="mr-2"
                  />
                  <Activity className="h-4 w-4 mr-1" />
                  Video
                </label>
              </div>
            </div>

            {/* Description */}
            <div className="space-y-2">
              <label className="text-sm font-medium text-gray-700">Descripción</label>
              <textarea
                value={multimediaForm.description}
                onChange={(e) => setMultimediaForm((prev) => ({ ...prev, description: e.target.value }))}
                placeholder="Describe esta foto o video de {pet.name}..."
                rows={3}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500"
                required
              />
            </div>

            {/* Guidelines */}
            <Card className="border-blue-200 bg-blue-50/50">
              <CardContent className="p-4">
                <h4 className="font-semibold text-blue-800 mb-2 flex items-center">
                  <AlertCircle className="h-4 w-4 mr-2" />
                  Pautas para Multimedia
                </h4>
                <ul className="text-sm text-blue-700 space-y-1">
                  <li>• Las imágenes deben mostrar claramente a {pet.name}</li>
                  <li>• Evita fotos borrosas o de mala calidad</li>
                  <li>• Los videos no deben exceder 2 minutos</li>
                  <li>• Contenido apropiado y respetuoso</li>
                  <li>• Ayuda a mostrar la personalidad de {pet.name}</li>
                </ul>
              </CardContent>
            </Card>

            {/* Action Buttons */}
            <div className="flex gap-4 pt-4 border-t">
              <Button type="button" onClick={() => setShowMultimediaDialog(false)} variant="outline" className="flex-1">
                Cancelar
              </Button>
              <Button
                type="submit"
                disabled={!multimediaForm.file && !multimediaForm.url}
                className="flex-1 bg-orange-600 hover:bg-orange-700 text-white"
              >
                <Camera className="mr-2 h-4 w-4" />
                Subir Multimedia
              </Button>
            </div>
          </form>
        </DialogContent>
      </Dialog>

      {/* Adoption Application Dialog */}
      <Dialog open={showApplicationDialog} onOpenChange={setShowApplicationDialog}>
        <DialogContent className="max-w-5xl max-h-[90vh] overflow-y-auto">
          <DialogHeader>
            <DialogTitle className="text-orange-800 flex items-center text-2xl">
              <Heart className="h-6 w-6 mr-2" />
              Proceso de Adopción para {pet.name}
            </DialogTitle>
            <DialogDescription className="text-orange-600 text-lg">
              Conoce todo el proceso que seguiremos juntos para encontrar el hogar perfecto para {pet.name}
            </DialogDescription>
          </DialogHeader>

          <div className="space-y-8">
            {/* Pet Summary */}
            <div className="bg-gradient-to-r from-orange-50 to-amber-50 p-6 rounded-xl border border-orange-200">
              <div className="flex items-center space-x-6">
                <Image
                  src={pet.images[0] || "/placeholder.svg"}
                  alt={pet.name}
                  width={120}
                  height={120}
                  className="w-30 h-30 rounded-xl object-cover shadow-lg"
                />
                <div className="flex-1">
                  <h3 className="text-2xl font-bold text-orange-800 mb-2">{pet.name}</h3>
                  <div className="grid grid-cols-2 gap-4 text-sm">
                    <div className="flex items-center">
                      <PawPrint className="h-4 w-4 mr-2 text-orange-600" />
                      <span>
                        {pet.breed} • {getSizeLabel(pet.size)} • {calculateAge(pet.birthDate)}
                      </span>
                    </div>
                    <div className="flex items-center">
                      <MapPin className="h-4 w-4 mr-2 text-orange-600" />
                      <span>{pet.shelter.name}</span>
                    </div>
                    <div className="flex items-center">
                      <Shield className="h-4 w-4 mr-2 text-green-600" />
                      <span>Vacunado y esterilizado</span>
                    </div>
                    <div className="flex items-center">
                      <Heart className="h-4 w-4 mr-2 text-red-600" />
                      <span>Aporte: ${pet.adoptionFee?.toLocaleString()}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            {/* Complete Adoption Process */}
            <div className="space-y-6">
              <div className="text-center">
                <h3 className="text-2xl font-bold text-orange-800 mb-2">🏠 Proceso Completo de Adopción</h3>
                <p className="text-orange-700">Un camino cuidadoso hacia una familia feliz</p>
              </div>

              {/* Process Steps */}
              <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                {/* Step 1: Application */}
                <Card className="border-blue-200 bg-blue-50/50">
                  <CardContent className="p-6">
                    <div className="flex items-center mb-4">
                      <div className="bg-blue-600 text-white rounded-full w-8 h-8 flex items-center justify-center font-bold mr-3">
                        1
                      </div>
                      <h4 className="text-lg font-semibold text-blue-800">Solicitud de Adopción</h4>
                    </div>
                    <div className="space-y-3 text-sm text-blue-700">
                      <div className="flex items-center">
                        <FileText className="h-4 w-4 mr-2" />
                        <span>Completas formulario detallado</span>
                      </div>
                      <div className="flex items-center">
                        <Clock className="h-4 w-4 mr-2" />
                        <span>Tiempo estimado: 15-20 minutos</span>
                      </div>
                      <div className="flex items-center">
                        <CheckCircle className="h-4 w-4 mr-2" />
                        <span>Estado: PENDING → Revisión inicial</span>
                      </div>
                    </div>
                    <div className="mt-4 p-3 bg-blue-100 rounded-lg">
                      <p className="text-xs text-blue-800">
                        <strong>Incluye:</strong> Motivación, experiencia, vivienda, familia, horarios, contactos de
                        emergencia
                      </p>
                    </div>
                  </CardContent>
                </Card>

                {/* Step 2: Veterinary Evaluation */}
                <Card className="border-purple-200 bg-purple-50/50">
                  <CardContent className="p-6">
                    <div className="flex items-center mb-4">
                      <div className="bg-purple-600 text-white rounded-full w-8 h-8 flex items-center justify-center font-bold mr-3">
                        2
                      </div>
                      <h4 className="text-lg font-semibold text-purple-800">Evaluación Veterinaria</h4>
                    </div>
                    <div className="space-y-3 text-sm text-purple-700">
                      <div className="flex items-center">
                        <Stethoscope className="h-4 w-4 mr-2" />
                        <span>Revisión por equipo especializado</span>
                      </div>
                      <div className="flex items-center">
                        <Clock className="h-4 w-4 mr-2" />
                        <span>Tiempo estimado: 2-3 días hábiles</span>
                      </div>
                      <div className="flex items-center">
                        <UserCheck className="h-4 w-4 mr-2" />
                        <span>Evaluación de compatibilidad</span>
                      </div>
                    </div>
                    <div className="mt-4 p-3 bg-purple-100 rounded-lg">
                      <p className="text-xs text-purple-800">
                        <strong>Evaluamos:</strong> Experiencia, espacio, tiempo disponible, compatibilidad con{" "}
                        {pet.name}
                      </p>
                    </div>
                  </CardContent>
                </Card>

                {/* Step 3: Home Visit */}
                <Card className="border-green-200 bg-green-50/50">
                  <CardContent className="p-6">
                    <div className="flex items-center mb-4">
                      <div className="bg-green-600 text-white rounded-full w-8 h-8 flex items-center justify-center font-bold mr-3">
                        3
                      </div>
                      <h4 className="text-lg font-semibold text-green-800">Visita Domiciliaria</h4>
                    </div>
                    <div className="space-y-3 text-sm text-green-700">
                      <div className="flex items-center">
                        <Home className="h-4 w-4 mr-2" />
                        <span>Verificación del hogar y familia</span>
                      </div>
                      <div className="flex items-center">
                        <Clock className="h-4 w-4 mr-2" />
                        <span>Duración: 45-60 minutos</span>
                      </div>
                      <div className="flex items-center">
                        <Calendar className="h-4 w-4 mr-2" />
                        <span>Programada según disponibilidad</span>
                      </div>
                    </div>
                    <div className="mt-4 p-3 bg-green-100 rounded-lg">
                      <p className="text-xs text-green-800">
                        <strong>Verificamos:</strong> Seguridad del espacio, preparación familiar, condiciones para{" "}
                        {pet.name}
                      </p>
                    </div>
                  </CardContent>
                </Card>

                {/* Step 4: Final Adoption */}
                <Card className="border-orange-200 bg-orange-50/50">
                  <CardContent className="p-6">
                    <div className="flex items-center mb-4">
                      <div className="bg-orange-600 text-white rounded-full w-8 h-8 flex items-center justify-center font-bold mr-3">
                        4
                      </div>
                      <h4 className="text-lg font-semibold text-orange-800">Adopción Final</h4>
                    </div>
                    <div className="space-y-3 text-sm text-orange-700">
                      <div className="flex items-center">
                        <Award className="h-4 w-4 mr-2" />
                        <span>Firma de contrato de adopción</span>
                      </div>
                      <div className="flex items-center">
                        <Heart className="h-4 w-4 mr-2" />
                        <span>Entrega oficial de {pet.name}</span>
                      </div>
                      <div className="flex items-center">
                        <FileText className="h-4 w-4 mr-2" />
                        <span>Documentación completa</span>
                      </div>
                    </div>
                    <div className="mt-4 p-3 bg-orange-100 rounded-lg">
                      <p className="text-xs text-orange-800">
                        <strong>Incluye:</strong> Carnet de vacunas, historial médico, kit de bienvenida, contactos de
                        emergencia
                      </p>
                    </div>
                  </CardContent>
                </Card>
              </div>

              {/* Post-Adoption Follow-up */}
              <Card className="border-pink-200 bg-pink-50/50">
                <CardContent className="p-6">
                  <div className="text-center mb-4">
                    <div className="bg-pink-600 text-white rounded-full w-12 h-12 flex items-center justify-center font-bold mx-auto mb-2">
                      <Heart className="h-6 w-6" />
                    </div>
                    <h4 className="text-xl font-semibold text-pink-800">Seguimiento Post-Adopción</h4>
                    <p className="text-pink-600">Acompañamiento continuo para una adopción exitosa</p>
                  </div>

                  <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <div className="text-center p-4 bg-white rounded-lg border border-pink-200">
                      <div className="bg-pink-100 rounded-full w-12 h-12 flex items-center justify-center mx-auto mb-2">
                        <Calendar className="h-6 w-6 text-pink-600" />
                      </div>
                      <h5 className="font-semibold text-pink-800 mb-1">3 Meses</h5>
                      <p className="text-xs text-pink-700">Primera evaluación de adaptación</p>
                    </div>
                    <div className="text-center p-4 bg-white rounded-lg border border-pink-200">
                      <div className="bg-pink-100 rounded-full w-12 h-12 flex items-center justify-center mx-auto mb-2">
                        <CheckCircle className="h-6 w-6 text-pink-600" />
                      </div>
                      <h5 className="font-semibold text-pink-800 mb-1">6 Meses</h5>
                      <p className="text-xs text-pink-700">Evaluación de bienestar y salud</p>
                    </div>
                    <div className="text-center p-4 bg-white rounded-lg border border-pink-200">
                      <div className="bg-pink-100 rounded-full w-12 h-12 flex items-center justify-center mx-auto mb-2">
                        <Award className="h-6 w-6 text-pink-600" />
                      </div>
                      <h5 className="font-semibold text-pink-800 mb-1">12 Meses</h5>
                      <p className="text-xs text-pink-700">Evaluación final de adopción exitosa</p>
                    </div>
                  </div>
                </CardContent>
              </Card>

              {/* Requirements and Commitments */}
              <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                <Card className="border-yellow-200 bg-yellow-50/50">
                  <CardContent className="p-6">
                    <h4 className="text-lg font-semibold text-yellow-800 mb-4 flex items-center">
                      <AlertCircle className="h-5 w-5 mr-2" />
                      Requisitos Importantes
                    </h4>
                    <ul className="space-y-2 text-sm text-yellow-700">
                      <li className="flex items-start">
                        <CheckCircle className="h-4 w-4 mr-2 mt-0.5 text-yellow-600" />
                        <span>Ser mayor de edad y tener documento de identidad</span>
                      </li>
                      <li className="flex items-start">
                        <CheckCircle className="h-4 w-4 mr-2 mt-0.5 text-yellow-600" />
                        <span>Contar con espacio adecuado para {pet.name}</span>
                      </li>
                      <li className="flex items-start">
                        <CheckCircle className="h-4 w-4 mr-2 mt-0.5 text-yellow-600" />
                        <span>Tener estabilidad económica para gastos veterinarios</span>
                      </li>
                      <li className="flex items-start">
                        <CheckCircle className="h-4 w-4 mr-2 mt-0.5 text-yellow-600" />
                        <span>Compromiso de cuidado de por vida</span>
                      </li>
                      <li className="flex items-start">
                        <CheckCircle className="h-4 w-4 mr-2 mt-0.5 text-yellow-600" />
                        <span>Aceptar visitas de seguimiento programadas</span>
                      </li>
                    </ul>
                  </CardContent>
                </Card>

                <Card className="border-red-200 bg-red-50/50">
                  <CardContent className="p-6">
                    <h4 className="text-lg font-semibold text-red-800 mb-4 flex items-center">
                      <Heart className="h-5 w-5 mr-2" />
                      Compromisos del Adoptante
                    </h4>
                    <ul className="space-y-2 text-sm text-red-700">
                      <li className="flex items-start">
                        <Heart className="h-4 w-4 mr-2 mt-0.5 text-red-600" />
                        <span>Proporcionar amor, cuidado y atención diaria</span>
                      </li>
                      <li className="flex items-start">
                        <Stethoscope className="h-4 w-4 mr-2 mt-0.5 text-red-600" />
                        <span>Mantener al día vacunas y cuidados veterinarios</span>
                      </li>
                      <li className="flex items-start">
                        <Home className="h-4 w-4 mr-2 mt-0.5 text-red-600" />
                        <span>Nunca abandonar o maltratar a {pet.name}</span>
                      </li>
                      <li className="flex items-start">
                        <Phone className="h-4 w-4 mr-2 mt-0.5 text-red-600" />
                        <span>Contactar al refugio ante cualquier problema</span>
                      </li>
                      <li className="flex items-start">
                        <RefreshCw className="h-4 w-4 mr-2 mt-0.5 text-red-600" />
                        <span>Devolver al refugio si no puede continuar el cuidado</span>
                      </li>
                    </ul>
                  </CardContent>
                </Card>
              </div>

              {/* Timeline Summary */}
              <Card className="border-gray-200 bg-gray-50/50">
                <CardContent className="p-6">
                  <h4 className="text-lg font-semibold text-gray-800 mb-4 text-center flex items-center justify-center">
                    <Clock className="h-5 w-5 mr-2" />
                    Tiempo Total Estimado del Proceso
                  </h4>
                  <div className="flex items-center justify-center space-x-8 text-center">
                    <div>
                      <div className="text-2xl font-bold text-blue-600">1-2</div>
                      <div className="text-sm text-gray-600">Semanas</div>
                      <div className="text-xs text-gray-500">Proceso completo</div>
                    </div>
                    <div className="h-12 w-px bg-gray-300"></div>
                    <div>
                      <div className="text-2xl font-bold text-green-600">24-48</div>
                      <div className="text-sm text-gray-600">Horas</div>
                      <div className="text-xs text-gray-500">Respuesta inicial</div>
                    </div>
                    <div className="h-12 w-px bg-gray-300"></div>
                    <div>
                      <div className="text-2xl font-bold text-purple-600">12</div>
                      <div className="text-sm text-gray-600">Meses</div>
                      <div className="text-xs text-gray-500">Seguimiento total</div>
                    </div>
                  </div>
                </CardContent>
              </Card>

              {/* Contact Information */}
              <Card className="border-orange-200 bg-orange-50/50">
                <CardContent className="p-6">
                  <h4 className="text-lg font-semibold text-orange-800 mb-4 text-center">
                    📞 Información de Contacto Durante el Proceso
                  </h4>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div className="text-center p-4 bg-white rounded-lg">
                      <h5 className="font-semibold text-orange-800 mb-2">{pet.shelter.name}</h5>
                      <div className="space-y-1 text-sm text-gray-600">
                        <div className="flex items-center justify-center">
                          <Phone className="h-4 w-4 mr-2" />
                          {pet.shelter.phone}
                        </div>
                        <div className="flex items-center justify-center">
                          <Mail className="h-4 w-4 mr-2" />
                          {pet.shelter.email}
                        </div>
                      </div>
                    </div>
                    <div className="text-center p-4 bg-white rounded-lg">
                      <h5 className="font-semibold text-orange-800 mb-2">Equipo Veterinario</h5>
                      <div className="space-y-1 text-sm text-gray-600">
                        <div className="flex items-center justify-center">
                          <Stethoscope className="h-4 w-4 mr-2" />
                          {pet.vaccineCard.veterinarian}
                        </div>
                        <div className="text-xs text-gray-500">Responsable del proceso</div>
                      </div>
                    </div>
                  </div>
                </CardContent>
              </Card>
            </div>

            {/* Action Buttons */}
            <div className="flex gap-4 pt-6 border-t">
              <Button
                type="button"
                onClick={() => setShowApplicationDialog(false)}
                variant="outline"
                className="flex-1 border-orange-600 text-orange-600 hover:bg-orange-50"
              >
                <ArrowLeft className="mr-2 h-4 w-4" />
                Volver a Ver a {pet.name}
              </Button>
              <Button
                onClick={() => {
                  setShowApplicationDialog(false)
                  handleAdoptionClick()
                }}
                className="flex-2 bg-gradient-to-r from-orange-600 to-red-600 hover:from-orange-700 hover:to-red-700 text-white shadow-lg hover:shadow-xl transition-all duration-300"
              >
                <Heart className="mr-2 h-4 w-4" />
                ¡Quiero Adoptar a {pet.name}!
              </Button>
            </div>
          </div>
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
