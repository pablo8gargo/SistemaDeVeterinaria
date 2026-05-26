"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Badge } from "@/components/ui/badge"
import {
  MapPin,
  Phone,
  Mail,
  Users,
  PawPrint,
  ArrowLeft,
  Calendar,
  Heart,
  Stethoscope,
  Clock,
  Star,
  Activity,
  UserCheck,
  FileText,
  Shield,
  AlertCircle,
  CheckCircle,
  XCircle,
} from "lucide-react"
import Image from "next/image"
import Link from "next/link"
import { useParams } from "next/navigation"

// Mock data for shelter details
const shelterDetails = {
  1: {
    id: 1,
    name: "Refugio Esperanza",
    address: "Calle 45 #12-34, Bogotá",
    phone: "+57 301 234 5678",
    email: "contacto@refugioesperanza.org",
    description:
      "Dedicados al rescate y cuidado de mascotas abandonadas desde hace 15 años. Nuestro compromiso es brindar amor, cuidado médico y encontrar el hogar perfecto para cada mascota.",
    foundedYear: 2009,
    capacity: 60,
    currentOccupancy: 45,
    image: "/placeholder.svg?height=400&width=600",
    coverImage: "/placeholder.svg?height=300&width=800",

    pets: [
      {
        id: 1,
        name: "Luna",
        birthDate: new Date("2022-03-15"),
        breed: "Labrador Mix",
        size: "MEDIUM",
        gender: "FEMALE",
        behaviorProfile: "Amigable, juguetona y muy cariñosa. Le encanta correr y jugar con otros perros.",
        image: "/placeholder.svg?height=300&width=400",
        vaccinated: true,
        sterilized: true,
        arrivalDate: new Date("2023-08-10"),
      },
      {
        id: 2,
        name: "Rocky",
        birthDate: new Date("2021-11-20"),
        breed: "Pastor Alemán",
        size: "LARGE",
        gender: "MALE",
        behaviorProfile: "Protector, leal y muy inteligente. Ideal para familias con experiencia en perros grandes.",
        image: "/placeholder.svg?height=300&width=400",
        vaccinated: true,
        sterilized: false,
        arrivalDate: new Date("2023-09-15"),
      },
      {
        id: 3,
        name: "Mia",
        birthDate: new Date("2023-01-10"),
        breed: "Mestizo",
        size: "SMALL",
        gender: "FEMALE",
        behaviorProfile: "Pequeña pero valiente, muy inteligente y fácil de entrenar.",
        image: "/placeholder.svg?height=300&width=400",
        vaccinated: true,
        sterilized: true,
        arrivalDate: new Date("2023-07-22"),
      },
    ],

    veterinarians: [
      {
        id: 1,
        name: "Dr. María González",
        email: "maria.gonzalez@refugioesperanza.org",
        phone: "+57 301 111 2222",
        licenseNumber: "VET-2018-001",
        speciality: { name: "Medicina General", description: "Atención médica integral para mascotas" },
        disponibilities: ["MORNING", "AFTERNOON"],
        yearsExperience: 8,
        image: "/placeholder.svg?height=200&width=200",
      },
      {
        id: 2,
        name: "Dr. Carlos Rodríguez",
        email: "carlos.rodriguez@refugioesperanza.org",
        phone: "+57 301 333 4444",
        licenseNumber: "VET-2020-045",
        speciality: { name: "Cirugía", description: "Especialista en procedimientos quirúrgicos" },
        disponibilities: ["MORNING", "EVENING"],
        yearsExperience: 6,
        image: "/placeholder.svg?height=200&width=200",
      },
      {
        id: 3,
        name: "Dra. Ana Martínez",
        email: "ana.martinez@refugioesperanza.org",
        phone: "+57 301 555 6666",
        licenseNumber: "VET-2019-023",
        speciality: { name: "Dermatología", description: "Tratamiento de enfermedades de la piel" },
        disponibilities: ["AFTERNOON", "EVENING"],
        yearsExperience: 7,
        image: "/placeholder.svg?height=200&width=200",
      },
    ],

    events: [
      {
        id: 1,
        name: "Jornada de Vacunación Gratuita",
        description:
          "Vacunación antirrábica y múltiple para mascotas de la comunidad. Nuestro equipo veterinario estará disponible para brindar atención médica preventiva.",
        date: new Date("2024-02-15"),
        shelter: {
          id: 1,
          name: "Refugio Esperanza",
          address: "Calle 45 #12-34, Bogotá",
          phone: "+57 301 234 5678",
          email: "contacto@refugioesperanza.org",
        },
      },
      {
        id: 2,
        name: "Feria de Adopción Especial",
        description:
          "Evento especial para encontrar hogares para nuestras mascotas. Conoce a todos nuestros peludos disponibles y encuentra tu compañero perfecto.",
        date: new Date("2024-01-28"),
        shelter: {
          id: 1,
          name: "Refugio Esperanza",
          address: "Calle 45 #12-34, Bogotá",
          phone: "+57 301 234 5678",
          email: "contacto@refugioesperanza.org",
        },
      },
      {
        id: 3,
        name: "Taller de Cuidado Responsable",
        description:
          "Educación sobre tenencia responsable de mascotas. Aprende sobre nutrición, cuidados básicos, entrenamiento y bienestar animal.",
        date: new Date("2024-01-20"),
        shelter: {
          id: 1,
          name: "Refugio Esperanza",
          address: "Calle 45 #12-34, Bogotá",
          phone: "+57 301 234 5678",
          email: "contacto@refugioesperanza.org",
        },
      },
      {
        id: 4,
        name: "Campaña de Esterilización",
        description:
          "Esterilización gratuita para mascotas de bajos recursos. Contribuyendo al control poblacional y la salud de las mascotas de la comunidad.",
        date: new Date("2024-02-10"),
        shelter: {
          id: 1,
          name: "Refugio Esperanza",
          address: "Calle 45 #12-34, Bogotá",
          phone: "+57 301 234 5678",
          email: "contacto@refugioesperanza.org",
        },
      },
      {
        id: 5,
        name: "Día de Puertas Abiertas",
        description:
          "Ven a conocer nuestras instalaciones, conoce a nuestro equipo y descubre cómo puedes ayudar. Tours guiados cada hora.",
        date: new Date("2024-02-20"),
        shelter: {
          id: 1,
          name: "Refugio Esperanza",
          address: "Calle 45 #12-34, Bogotá",
          phone: "+57 301 234 5678",
          email: "contacto@refugioesperanza.org",
        },
      },
      {
        id: 6,
        name: "Colecta de Alimentos y Medicinas",
        description:
          "Jornada de recolección de donaciones para el cuidado de nuestras mascotas. Acepta alimentos, medicinas, juguetes y mantas.",
        date: new Date("2024-03-05"),
        shelter: {
          id: 1,
          name: "Refugio Esperanza",
          address: "Calle 45 #12-34, Bogotá",
          phone: "+57 301 234 5678",
          email: "contacto@refugioesperanza.org",
        },
      },
    ],

    arrivals: [
      {
        id: 1,
        petName: "Luna",
        arrivalDate: new Date("2023-08-10"),
        reason: "ABANDONMENT",
        condition: "GOOD",
        rescuer: "Ciudadano anónimo",
        notes: "Encontrada en la calle, bien alimentada pero sin collar",
      },
      {
        id: 2,
        petName: "Rocky",
        arrivalDate: new Date("2023-09-15"),
        reason: "SURRENDER",
        condition: "FAIR",
        rescuer: "Familia anterior",
        notes: "Entregado por mudanza, necesita socialización",
      },
      {
        id: 3,
        petName: "Mia",
        arrivalDate: new Date("2023-07-22"),
        reason: "RESCUE",
        condition: "POOR",
        rescuer: "Equipo de rescate",
        notes: "Rescatada de situación de maltrato, rehabilitada completamente",
      },
    ],
  },
}

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

function getEventStatusBadge(status: string) {
  const statusConfig = {
    UPCOMING: { label: "Próximo", color: "bg-blue-600", icon: Clock },
    ACTIVE: { label: "Activo", color: "bg-green-600", icon: Activity },
    COMPLETED: { label: "Completado", color: "bg-gray-600", icon: CheckCircle },
    CANCELLED: { label: "Cancelado", color: "bg-red-600", icon: XCircle },
  }

  const config = statusConfig[status as keyof typeof statusConfig] || statusConfig.UPCOMING
  const Icon = config.icon

  return (
    <Badge className={`${config.color} text-white`}>
      <Icon className="h-3 w-3 mr-1" />
      {config.label}
    </Badge>
  )
}

function getDisponibilityLabel(disponibility: string): string {
  const disponibilityMap: { [key: string]: string } = {
    MORNING: "Mañana",
    AFTERNOON: "Tarde",
    EVENING: "Noche",
    FULL_TIME: "Tiempo Completo",
  }
  return disponibilityMap[disponibility] || disponibility
}

export default function ShelterDetailPage() {
  const params = useParams()
  const shelterId = Number.parseInt(params.id as string)
  const shelter = shelterDetails[shelterId as keyof typeof shelterDetails]
  const [activeTab, setActiveTab] = useState("pets")

  if (!shelter) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-orange-50 to-amber-50 flex items-center justify-center">
        <div className="text-center">
          <AlertCircle className="h-16 w-16 text-orange-600 mx-auto mb-4" />
          <h2 className="text-2xl font-bold text-orange-800 mb-2">Refugio no encontrado</h2>
          <p className="text-orange-700 mb-4">El refugio que buscas no existe o ha sido removido.</p>
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
              <Button variant="ghost" size="sm" asChild className="text-orange-700 hover:text-orange-900">
                <Link href="/shelter">
                  <ArrowLeft className="h-4 w-4 mr-2" />
                  Volver a Refugios
                </Link>
              </Button>
              <div className="h-6 w-px bg-orange-200"></div>
              <div className="flex items-center space-x-3">
                <div className="bg-orange-100 p-2 rounded-full">
                  <PawPrint className="h-6 w-6 text-orange-600" />
                </div>
                <h1 className="text-2xl font-bold text-orange-800">Sistema de Veterinaria</h1>
              </div>
            </div>
            <Button className="bg-green-600 hover:bg-green-700 text-white shadow-lg hover:shadow-xl transition-all duration-300">
              <Heart className="mr-2 h-4 w-4" />
              Hacer Donación
            </Button>
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <section className="relative">
        <div className="relative h-80 overflow-hidden">
          <Image
            src={shelter.coverImage || "/placeholder.svg"}
            alt={shelter.name}
            width={800}
            height={300}
            className="w-full h-full object-cover"
          />
          <div className="absolute inset-0 bg-gradient-to-t from-black/60 via-black/20 to-transparent"></div>
          <div className="absolute bottom-0 left-0 right-0 p-8">
            <div className="container mx-auto">
              <div className="flex items-end justify-between">
                <div className="text-white">
                  <h1 className="text-4xl font-bold mb-2">{shelter.name}</h1>
                  <p className="text-lg opacity-90 max-w-2xl">{shelter.description}</p>
                </div>
                <div className="text-right text-white">
                  <div className="bg-white/20 backdrop-blur-sm rounded-lg p-4">
                    <div className="text-2xl font-bold">{shelter.foundedYear}</div>
                    <div className="text-sm opacity-90">Fundado</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Quick Stats */}
      <section className="py-8 bg-white/70 backdrop-blur-sm border-b border-orange-100">
        <div className="container mx-auto px-4">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
            <div className="text-center">
              <div className="bg-orange-100 w-12 h-12 rounded-full flex items-center justify-center mx-auto mb-2">
                <PawPrint className="h-6 w-6 text-orange-600" />
              </div>
              <div className="text-2xl font-bold text-orange-600">{shelter.pets.length}</div>
              <div className="text-sm text-orange-800">Mascotas</div>
            </div>
            <div className="text-center">
              <div className="bg-blue-100 w-12 h-12 rounded-full flex items-center justify-center mx-auto mb-2">
                <Stethoscope className="h-6 w-6 text-blue-600" />
              </div>
              <div className="text-2xl font-bold text-blue-600">{shelter.veterinarians.length}</div>
              <div className="text-sm text-orange-800">Veterinarios</div>
            </div>
            <div className="text-center">
              <div className="bg-green-100 w-12 h-12 rounded-full flex items-center justify-center mx-auto mb-2">
                <Calendar className="h-6 w-6 text-green-600" />
              </div>
              <div className="text-2xl font-bold text-green-600">{shelter.events.length}</div>
              <div className="text-sm text-orange-800">Eventos</div>
            </div>
            <div className="text-center">
              <div className="bg-purple-100 w-12 h-12 rounded-full flex items-center justify-center mx-auto mb-2">
                <Users className="h-6 w-6 text-purple-600" />
              </div>
              <div className="text-2xl font-bold text-purple-600">{shelter.capacity}</div>
              <div className="text-sm text-orange-800">Capacidad</div>
            </div>
          </div>
        </div>
      </section>

      {/* Contact Info */}
      <section className="py-6 bg-orange-50/50">
        <div className="container mx-auto px-4">
          <div className="flex flex-wrap gap-6 justify-center">
            <div className="flex items-center text-orange-700">
              <MapPin className="h-5 w-5 mr-2" />
              <span>{shelter.address}</span>
            </div>
            <div className="flex items-center text-orange-700">
              <Phone className="h-5 w-5 mr-2" />
              <span>{shelter.phone}</span>
            </div>
            <div className="flex items-center text-orange-700">
              <Mail className="h-5 w-5 mr-2" />
              <span>{shelter.email}</span>
            </div>
          </div>
        </div>
      </section>

      {/* Main Content Tabs */}
      <section className="py-16 px-4">
        <div className="container mx-auto">
          <Tabs value={activeTab} onValueChange={setActiveTab} className="w-full">
            <TabsList className="grid w-full grid-cols-4 max-w-2xl mx-auto mb-12 bg-orange-100/80 backdrop-blur-sm shadow-lg">
              <TabsTrigger
                value="pets"
                className="data-[state=active]:bg-orange-600 data-[state=active]:text-white transition-all duration-300 flex items-center text-sm"
              >
                <PawPrint className="h-4 w-4 mr-1" />
                Mascotas
              </TabsTrigger>
              <TabsTrigger
                value="veterinarians"
                className="data-[state=active]:bg-orange-600 data-[state=active]:text-white transition-all duration-300 flex items-center text-sm"
              >
                <Stethoscope className="h-4 w-4 mr-1" />
                Veterinarios
              </TabsTrigger>
              <TabsTrigger
                value="events"
                className="data-[state=active]:bg-orange-600 data-[state=active]:text-white transition-all duration-300 flex items-center text-sm"
              >
                <Calendar className="h-4 w-4 mr-1" />
                Eventos
              </TabsTrigger>
              <TabsTrigger
                value="arrivals"
                className="data-[state=active]:bg-orange-600 data-[state=active]:text-white transition-all duration-300 flex items-center text-sm"
              >
                <FileText className="h-4 w-4 mr-1" />
                Llegadas
              </TabsTrigger>
            </TabsList>

            {/* Pets Tab */}
            <TabsContent value="pets" className="space-y-8">
              <div className="text-center mb-8">
                <h3 className="text-2xl font-bold text-orange-800 mb-2">Mascotas en el Refugio</h3>
                <p className="text-orange-700">
                  Conoce a todas las mascotas que actualmente están bajo nuestro cuidado
                </p>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {shelter.pets.map((pet) => (
                  <Card
                    key={pet.id}
                    className="overflow-hidden hover:shadow-xl transition-all duration-300 border-orange-200 group"
                  >
                    <div className="relative">
                      <Image
                        src={pet.image || "/placeholder.svg"}
                        alt={pet.name}
                        width={400}
                        height={300}
                        className="w-full h-48 object-cover group-hover:scale-105 transition-transform duration-300"
                      />
                      <div className="absolute top-4 right-4 flex gap-2">
                        {pet.vaccinated && (
                          <Badge className="bg-green-600 text-white">
                            <Shield className="h-3 w-3 mr-1" />
                            Vacunado
                          </Badge>
                        )}
                        {pet.sterilized && (
                          <Badge className="bg-blue-600 text-white">
                            <CheckCircle className="h-3 w-3 mr-1" />
                            Esterilizado
                          </Badge>
                        )}
                      </div>
                    </div>
                    <CardHeader>
                      <CardTitle className="text-orange-800">{pet.name}</CardTitle>
                      <CardDescription className="text-orange-600">
                        {pet.breed} • {getSizeLabel(pet.size)} • {getGenderLabel(pet.gender)}
                      </CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-3">
                      <div className="flex items-center justify-between text-sm">
                        <div className="flex items-center text-orange-600">
                          <Calendar className="h-4 w-4 mr-1" />
                          {calculateAge(pet.birthDate)}
                        </div>
                        <div className="text-gray-500">Llegó: {pet.arrivalDate.toLocaleDateString()}</div>
                      </div>
                      <p className="text-sm text-gray-600">{pet.behaviorProfile}</p>
                      <Button className="w-full bg-orange-600 hover:bg-orange-700 text-white" asChild>
                        <Link href={`/pet/${pet.id}`}>
                          <Heart className="mr-2 h-4 w-4" />
                          Adoptar a {pet.name}
                        </Link>
                      </Button>
                    </CardContent>
                  </Card>
                ))}
              </div>
            </TabsContent>

            {/* Veterinarians Tab */}
            <TabsContent value="veterinarians" className="space-y-8">
              <div className="text-center mb-8">
                <h3 className="text-2xl font-bold text-orange-800 mb-2">Equipo Veterinario</h3>
                <p className="text-orange-700">Profesionales dedicados al cuidado y bienestar de nuestras mascotas</p>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {shelter.veterinarians.map((vet) => (
                  <Card key={vet.id} className="hover:shadow-xl transition-all duration-300 border-orange-200">
                    <CardHeader className="text-center">
                      <div className="mx-auto mb-4">
                        <Image
                          src={vet.image || "/placeholder.svg"}
                          alt={vet.name}
                          width={200}
                          height={200}
                          className="w-24 h-24 rounded-full mx-auto object-cover"
                        />
                      </div>
                      <CardTitle className="text-orange-800">{vet.name}</CardTitle>
                      <CardDescription className="text-orange-600">{vet.speciality.name}</CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-3">
                      <div className="bg-blue-50 p-3 rounded-lg">
                        <div className="text-sm font-medium text-blue-800 mb-1">Especialidad</div>
                        <div className="text-sm text-blue-600">{vet.speciality.description}</div>
                      </div>

                      <div className="space-y-2">
                        <div className="flex items-center text-sm text-gray-600">
                          <FileText className="h-4 w-4 mr-2 text-orange-600" />
                          Licencia: {vet.licenseNumber}
                        </div>
                        <div className="flex items-center text-sm text-gray-600">
                          <Star className="h-4 w-4 mr-2 text-orange-600" />
                          {vet.yearsExperience} años de experiencia
                        </div>
                        <div className="flex items-center text-sm text-gray-600">
                          <Phone className="h-4 w-4 mr-2 text-orange-600" />
                          {vet.phone}
                        </div>
                        <div className="flex items-center text-sm text-gray-600">
                          <Mail className="h-4 w-4 mr-2 text-orange-600" />
                          {vet.email}
                        </div>
                      </div>

                      <div className="bg-green-50 p-3 rounded-lg">
                        <div className="text-sm font-medium text-green-800 mb-2">Disponibilidad</div>
                        <div className="flex flex-wrap gap-1">
                          {vet.disponibilities.map((disp, index) => (
                            <Badge key={index} variant="outline" className="text-xs border-green-600 text-green-600">
                              <Clock className="h-3 w-3 mr-1" />
                              {getDisponibilityLabel(disp)}
                            </Badge>
                          ))}
                        </div>
                      </div>

                      <Button className="w-full bg-blue-600 hover:bg-blue-700 text-white" asChild>
                        <Link href={`/veterinarian/${vet.id}`}>
                          <UserCheck className="mr-2 h-4 w-4" />
                          Ver Perfil Completo
                        </Link>
                      </Button>
                    </CardContent>
                  </Card>
                ))}
              </div>
            </TabsContent>

            {/* Events Tab */}
            <TabsContent value="events" className="space-y-8">
              <div className="text-center mb-12">
                <div className="flex justify-center mb-6">
                  <div className="bg-gradient-to-r from-purple-100 to-pink-100 p-4 rounded-full">
                    <Calendar className="h-8 w-8 text-purple-600" />
                  </div>
                </div>
                <h3 className="text-3xl font-bold text-orange-800 mb-4">Eventos del Refugio</h3>
                <p className="text-orange-700 max-w-2xl mx-auto">
                  Únete a nuestras actividades, jornadas médicas y eventos especiales. Cada evento es una oportunidad
                  para ayudar y conectar con nuestra comunidad.
                </p>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                {shelter.events.map((event, index) => {
                  const eventDate = new Date(event.date)
                  const isUpcoming = eventDate > new Date()
                  const isPast = eventDate < new Date()
                  const isToday = eventDate.toDateString() === new Date().toDateString()

                  // Colores dinámicos basados en el tipo de evento
                  const eventColors = [
                    {
                      bg: "from-blue-500 to-blue-600",
                      accent: "bg-blue-100",
                      text: "text-blue-600",
                      border: "border-blue-200",
                    },
                    {
                      bg: "from-green-500 to-green-600",
                      accent: "bg-green-100",
                      text: "text-green-600",
                      border: "border-green-200",
                    },
                    {
                      bg: "from-purple-500 to-purple-600",
                      accent: "bg-purple-100",
                      text: "text-purple-600",
                      border: "border-purple-200",
                    },
                    {
                      bg: "from-pink-500 to-pink-600",
                      accent: "bg-pink-100",
                      text: "text-pink-600",
                      border: "border-pink-200",
                    },
                    {
                      bg: "from-indigo-500 to-indigo-600",
                      accent: "bg-indigo-100",
                      text: "text-indigo-600",
                      border: "border-indigo-200",
                    },
                    {
                      bg: "from-teal-500 to-teal-600",
                      accent: "bg-teal-100",
                      text: "text-teal-600",
                      border: "border-teal-200",
                    },
                  ]

                  const colorScheme = eventColors[index % eventColors.length]

                  return (
                    <Card
                      key={event.id}
                      className={`overflow-hidden hover:shadow-2xl transition-all duration-500 ${colorScheme.border} group hover:border-opacity-50 transform hover:-translate-y-2 ${isPast ? "opacity-75" : ""}`}
                    >
                      {/* Header con gradiente */}
                      <div className={`bg-gradient-to-r ${colorScheme.bg} p-6 text-white relative overflow-hidden`}>
                        <div className="absolute top-0 right-0 w-32 h-32 bg-white/10 rounded-full -translate-y-16 translate-x-16"></div>
                        <div className="absolute bottom-0 left-0 w-24 h-24 bg-white/10 rounded-full translate-y-12 -translate-x-12"></div>

                        <div className="relative z-10">
                          <div className="flex items-start justify-between mb-4">
                            <div className="flex-1">
                              <h4 className="text-xl font-bold mb-2 leading-tight">{event.name}</h4>
                              {isToday && (
                                <Badge className="bg-yellow-400 text-yellow-900 mb-2">
                                  <Clock className="h-3 w-3 mr-1" />
                                  ¡HOY!
                                </Badge>
                              )}
                              {isUpcoming && !isToday && (
                                <Badge className="bg-white/20 text-white mb-2">
                                  <Calendar className="h-3 w-3 mr-1" />
                                  Próximo
                                </Badge>
                              )}
                              {isPast && (
                                <Badge className="bg-gray-500 text-white mb-2">
                                  <CheckCircle className="h-3 w-3 mr-1" />
                                  Finalizado
                                </Badge>
                              )}
                            </div>
                            <div className="text-right">
                              <div className="bg-white/20 backdrop-blur-sm rounded-lg p-3">
                                <div className="text-2xl font-bold">{eventDate.getDate()}</div>
                                <div className="text-sm opacity-90">
                                  {eventDate.toLocaleDateString("es-ES", { month: "short" }).toUpperCase()}
                                </div>
                              </div>
                            </div>
                          </div>

                          <div className="flex items-center text-white/90 text-sm">
                            <Calendar className="h-4 w-4 mr-2" />
                            {eventDate.toLocaleDateString("es-ES", {
                              weekday: "long",
                              year: "numeric",
                              month: "long",
                              day: "numeric",
                            })}
                          </div>
                        </div>
                      </div>

                      {/* Contenido */}
                      <CardContent className="p-6 space-y-4">
                        <p className="text-gray-600 leading-relaxed">{event.description}</p>

                        {/* Información del refugio */}
                        <div className={`${colorScheme.accent} p-4 rounded-lg border ${colorScheme.border}`}>
                          <div className="flex items-start space-x-3">
                            <div className={`${colorScheme.accent} p-2 rounded-full`}>
                              <MapPin className={`h-4 w-4 ${colorScheme.text}`} />
                            </div>
                            <div className="flex-1">
                              <h5 className={`font-semibold ${colorScheme.text} mb-1`}>Organizado por</h5>
                              <p className="text-sm font-medium text-gray-800">{event.shelter.name}</p>
                              <p className="text-xs text-gray-600">{event.shelter.address}</p>
                            </div>
                          </div>
                        </div>

                        {/* Información de contacto */}
                        <div className="space-y-2">
                          <div className="flex items-center text-sm text-gray-600">
                            <Phone className={`h-4 w-4 mr-2 ${colorScheme.text}`} />
                            {event.shelter.phone}
                          </div>
                          <div className="flex items-center text-sm text-gray-600">
                            <Mail className={`h-4 w-4 mr-2 ${colorScheme.text}`} />
                            {event.shelter.email}
                          </div>
                        </div>

                        {/* Botón de acción */}
                        <div className="pt-4">
                          {isUpcoming || isToday ? (
                            <Button
                              className={`w-full bg-gradient-to-r ${colorScheme.bg} hover:shadow-lg text-white transition-all duration-300 transform hover:scale-105`}
                            >
                              <Calendar className="mr-2 h-4 w-4" />
                              {isToday ? "Participar Hoy" : "Registrarse al Evento"}
                            </Button>
                          ) : (
                            <Button
                              variant="outline"
                              className={`w-full ${colorScheme.border} ${colorScheme.text} hover:${colorScheme.accent}`}
                              disabled
                            >
                              <CheckCircle className="mr-2 h-4 w-4" />
                              Evento Finalizado
                            </Button>
                          )}
                        </div>
                      </CardContent>

                      {/* Indicador de estado en la esquina */}
                      <div className="absolute top-4 left-4">
                        {isToday && <div className="w-3 h-3 bg-yellow-400 rounded-full animate-pulse"></div>}
                        {isUpcoming && !isToday && <div className="w-3 h-3 bg-green-400 rounded-full"></div>}
                        {isPast && <div className="w-3 h-3 bg-gray-400 rounded-full"></div>}
                      </div>
                    </Card>
                  )
                })}
              </div>

              {/* Sección de llamada a la acción */}
              <div className="mt-16 bg-gradient-to-r from-orange-100 via-amber-50 to-orange-100 rounded-2xl p-8 text-center">
                <div className="flex justify-center mb-6">
                  <div className="bg-gradient-to-r from-orange-500 to-amber-500 p-4 rounded-full">
                    <Users className="h-8 w-8 text-white" />
                  </div>
                </div>
                <h4 className="text-2xl font-bold text-orange-800 mb-4">¿Quieres organizar un evento?</h4>
                <p className="text-orange-700 mb-6 max-w-2xl mx-auto">
                  Si tienes ideas para eventos que beneficien a nuestras mascotas o quieres colaborar en la
                  organización, nos encantaría escucharte. Juntos podemos hacer la diferencia.
                </p>
                <div className="flex flex-col sm:flex-row gap-4 justify-center">
                  <Button className="bg-gradient-to-r from-orange-600 to-amber-600 hover:from-orange-700 hover:to-amber-700 text-white shadow-lg hover:shadow-xl transition-all duration-300">
                    <Mail className="mr-2 h-4 w-4" />
                    Proponer Evento
                  </Button>
                  <Button
                    variant="outline"
                    className="border-orange-600 text-orange-600 hover:bg-orange-50 bg-transparent"
                  >
                    <Users className="mr-2 h-4 w-4" />
                    Ser Voluntario
                  </Button>
                </div>
              </div>
            </TabsContent>

            {/* Arrivals Tab */}
            <TabsContent value="arrivals" className="space-y-8">
              <div className="text-center mb-8">
                <h3 className="text-2xl font-bold text-orange-800 mb-2">Registro de Llegadas</h3>
                <p className="text-orange-700">
                  Historial de mascotas que han llegado al refugio y las circunstancias de su rescate
                </p>
              </div>

              <div className="space-y-4">
                {shelter.arrivals.map((arrival) => (
                  <Card key={arrival.id} className="hover:shadow-lg transition-all duration-300 border-orange-200">
                    <CardContent className="p-6">
                      <div className="flex items-start justify-between mb-4">
                        <div>
                          <h4 className="text-lg font-semibold text-orange-800 mb-1">{arrival.petName}</h4>
                          <div className="flex items-center text-sm text-gray-600">
                            <Calendar className="h-4 w-4 mr-2 text-orange-600" />
                            Llegada: {arrival.arrivalDate.toLocaleDateString()}
                          </div>
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
                        <div className="space-y-2">
                          <div className="flex items-center text-sm">
                            <AlertCircle className="h-4 w-4 mr-2 text-orange-600" />
                            <span className="font-medium">Motivo:</span>
                            <span className="ml-2 text-gray-600">
                              {arrival.reason === "ABANDONMENT"
                                ? "Abandono"
                                : arrival.reason === "SURRENDER"
                                  ? "Entrega Voluntaria"
                                  : "Rescate"}
                            </span>
                          </div>
                          <div className="flex items-center text-sm">
                            <Users className="h-4 w-4 mr-2 text-orange-600" />
                            <span className="font-medium">Rescatista:</span>
                            <span className="ml-2 text-gray-600">{arrival.rescuer}</span>
                          </div>
                        </div>
                      </div>

                      {arrival.notes && (
                        <div className="bg-orange-50 p-3 rounded-lg">
                          <div className="flex items-start">
                            <FileText className="h-4 w-4 mr-2 text-orange-600 mt-0.5" />
                            <div>
                              <span className="text-sm font-medium text-orange-800">Notas:</span>
                              <p className="text-sm text-orange-700 mt-1">{arrival.notes}</p>
                            </div>
                          </div>
                        </div>
                      )}
                    </CardContent>
                  </Card>
                ))}
              </div>
            </TabsContent>
          </Tabs>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-16 bg-gradient-to-r from-orange-600 via-amber-600 to-orange-700 text-white relative overflow-hidden">
        <div className="absolute inset-0 bg-black/10"></div>
        <div className="container mx-auto px-4 text-center relative z-10">
          <div className="flex justify-center mb-6">
            <div className="bg-white/20 p-4 rounded-full backdrop-blur-sm">
              <Heart className="h-12 w-12 text-white" />
            </div>
          </div>
          <h3 className="text-3xl font-bold mb-6">Apoya a {shelter.name}</h3>
          <p className="text-lg mb-8 max-w-2xl mx-auto opacity-90 leading-relaxed">
            Tu ayuda hace la diferencia. Puedes adoptar, hacer donaciones o ser voluntario para apoyar nuestra misión.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Button
              size="lg"
              className="bg-white text-orange-600 hover:bg-gray-100 px-8 py-3 shadow-xl hover:shadow-2xl transition-all duration-300 transform hover:-translate-y-1"
            >
              <Heart className="mr-2 h-5 w-5" />
              Hacer Donación
            </Button>
            <Button
              size="lg"
              variant="outline"
              className="border-white text-white hover:bg-white/10 px-8 py-3 shadow-xl hover:shadow-2xl transition-all duration-300 transform hover:-translate-y-1 backdrop-blur-sm bg-transparent"
            >
              <Users className="mr-2 h-5 w-5" />
              Ser Voluntario
            </Button>
          </div>
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
