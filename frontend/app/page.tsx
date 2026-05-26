"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Badge } from "@/components/ui/badge"
import { Heart, MapPin, Phone, Mail, Calendar, Users, Award, PawPrint, Shield } from "lucide-react"
import Image from "next/image"
import Link from "next/link"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import { CheckCircle, Clock, Home, FileText, Stethoscope } from "lucide-react"

// Mock data based on the DTOs
const mockPets = [
  {
    id: 1,
    name: "Luna",
    birthDate: new Date("2022-03-15"),
    breed: "Labrador Mix",
    size: "MEDIUM",
    gender: "FEMALE",
    behaviorProfile: "Amigable, juguetona y muy cariñosa. Le encanta correr y jugar con otros perros.",
    shelter: {
      id: 1,
      name: "Refugio Esperanza",
      address: "Calle 45 #12-34, Bogotá",
      phone: "+57 301 234 5678",
      email: "contacto@refugioesperanza.org",
    },
    image: "/placeholder.svg?height=300&width=400",
  },
  {
    id: 2,
    name: "Max",
    birthDate: new Date("2021-08-20"),
    breed: "Golden Retriever",
    size: "LARGE",
    gender: "MALE",
    behaviorProfile: "Tranquilo, obediente y perfecto para familias con niños.",
    shelter: {
      id: 2,
      name: "Hogar Feliz",
      address: "Carrera 15 #67-89, Medellín",
      phone: "+57 304 567 8901",
      email: "info@hogarfeliz.com",
    },
    image: "/placeholder.svg?height=300&width=400",
  },
  {
    id: 3,
    name: "Mia",
    birthDate: new Date("2023-01-10"),
    breed: "Mestizo",
    size: "SMALL",
    gender: "FEMALE",
    behaviorProfile: "Pequeña pero valiente, muy inteligente y fácil de entrenar.",
    shelter: {
      id: 1,
      name: "Refugio Esperanza",
      address: "Calle 45 #12-34, Bogotá",
      phone: "+57 301 234 5678",
      email: "contacto@refugioesperanza.org",
    },
    image: "/placeholder.svg?height=300&width=400",
  },
]

const mockShelters = [
  {
    id: 1,
    name: "Refugio Esperanza",
    address: "Calle 45 #12-34, Bogotá",
    phone: "+57 301 234 5678",
    email: "contacto@refugioesperanza.org",
    description: "Dedicados al rescate y cuidado de mascotas abandonadas desde hace 15 años.",
    petsCount: 45,
    image: "/placeholder.svg?height=200&width=300",
  },
  {
    id: 2,
    name: "Hogar Feliz",
    address: "Carrera 15 #67-89, Medellín",
    phone: "+57 304 567 8901",
    email: "info@hogarfeliz.com",
    description: "Un refugio familiar que brinda amor y cuidado especializado a cada mascota.",
    petsCount: 32,
    image: "/placeholder.svg?height=200&width=300",
  },
  {
    id: 3,
    name: "Patitas Unidas",
    address: "Avenida 30 #78-12, Cali",
    phone: "+57 315 678 9012",
    email: "adopciones@patitasunidas.org",
    description: "Trabajamos incansablemente para encontrar el hogar perfecto para cada mascota.",
    petsCount: 28,
    image: "/placeholder.svg?height=200&width=300",
  },
]

const successStories = [
  {
    id: 1,
    petName: "Rocky",
    ownerName: "María González",
    story:
      "Rocky llegó a mi vida cuando más lo necesitaba. Su amor incondicional me ayudó a superar momentos difíciles.",
    image: "/placeholder.svg?height=150&width=150",
    months: 8,
  },
  {
    id: 2,
    petName: "Bella",
    ownerName: "Carlos Rodríguez",
    story:
      "Bella se adaptó perfectamente a nuestra familia. Los niños la adoran y ella los protege como una verdadera guardiana.",
    image: "/placeholder.svg?height=150&width=150",
    months: 12,
  },
  {
    id: 3,
    petName: "Coco",
    ownerName: "Ana Martínez",
    story: "Adoptar a Coco fue la mejor decisión. Su energía y alegría transformaron completamente nuestro hogar.",
    image: "/placeholder.svg?height=150&width=150",
    months: 6,
  },
]

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

export default function HomePage() {
  const [activeTab, setActiveTab] = useState("pets")
  const [showAdoptionProcess, setShowAdoptionProcess] = useState(false)

  return (
    <div className="min-h-screen bg-gradient-to-br from-orange-50 to-amber-50">
      {/* Header */}
      <header className="bg-white/90 backdrop-blur-md border-b border-orange-100 sticky top-0 z-50 shadow-sm">
        <div className="container mx-auto px-4 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-3 group">
              <div className="bg-orange-100 p-2 rounded-full group-hover:bg-orange-200 transition-colors">
                <PawPrint className="h-6 w-6 text-orange-600" />
              </div>
              <h1 className="text-2xl font-bold text-orange-800">Sistema de Veterinaria</h1>
            </div>
            <nav className="hidden md:flex items-center space-x-6">
              <Link
                href="#mascotas"
                className="text-orange-700 hover:text-orange-900 font-medium flex items-center transition-colors"
              >
                <PawPrint className="h-4 w-4 mr-1" />
                Mascotas
              </Link>
              <Link
                href="/shelter"
                className="text-orange-700 hover:text-orange-900 font-medium flex items-center transition-colors"
              >
                <MapPin className="h-4 w-4 mr-1" />
                Refugios
              </Link>
              <Link
                href="#historias"
                className="text-orange-700 hover:text-orange-900 font-medium flex items-center transition-colors"
              >
                <Award className="h-4 w-4 mr-1" />
                Historias
              </Link>
              <Button
                className="bg-green-600 hover:bg-green-700 text-white shadow-lg hover:shadow-xl transition-all duration-300 transform hover:-translate-y-0.5"
                asChild
              >
                <Link href="/register">
                  <Users className="mr-2 h-4 w-4" />
                  Registrarse como Adoptante
                </Link>
              </Button>
            </nav>
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <section className="py-20 px-4 relative overflow-hidden">
        <div className="absolute inset-0 bg-gradient-to-br from-orange-50 via-amber-50 to-orange-100 opacity-50"></div>
        <div className="absolute top-10 left-10 text-orange-200 opacity-30">
          <Heart className="h-16 w-16" />
        </div>
        <div className="absolute bottom-10 right-10 text-orange-200 opacity-30">
          <PawPrint className="h-20 w-20" />
        </div>
        <div className="container mx-auto text-center relative z-10">
          <div className="flex justify-center mb-6">
            <div className="bg-orange-100 p-4 rounded-full">
              <PawPrint className="h-12 w-12 text-orange-600" />
            </div>
          </div>
          <h2 className="text-5xl md:text-6xl font-bold text-orange-800 mb-6">Encuentra tu compañero perfecto</h2>
          <p className="text-xl text-orange-700 mb-8 max-w-2xl mx-auto leading-relaxed">
            Miles de mascotas esperan encontrar un hogar lleno de amor. Únete a nuestra comunidad y cambia una vida para
            siempre.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Dialog open={showAdoptionProcess} onOpenChange={setShowAdoptionProcess}>
              <DialogTrigger asChild>
                <Button
                  size="lg"
                  className="bg-orange-600 hover:bg-orange-700 text-white px-8 py-3 shadow-lg hover:shadow-xl transition-all duration-300 transform hover:-translate-y-1"
                >
                  <Heart className="mr-2 h-5 w-5" />
                  Adoptar Ahora
                </Button>
              </DialogTrigger>
              <DialogContent className="max-w-4xl max-h-[90vh] overflow-y-auto">
                <DialogHeader>
                  <DialogTitle className="text-2xl font-bold text-orange-800 flex items-center">
                    <Heart className="mr-3 h-6 w-6 text-red-500" />
                    Proceso Completo de Adopción
                  </DialogTitle>
                  <DialogDescription className="text-orange-600">
                    Conoce todos los pasos para adoptar una mascota de manera responsable
                  </DialogDescription>
                </DialogHeader>

                <div className="space-y-6">
                  {/* Pasos del Proceso */}
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <Card className="border-orange-200">
                      <CardHeader className="pb-3">
                        <CardTitle className="text-lg flex items-center text-orange-800">
                          <FileText className="mr-2 h-5 w-5 text-blue-600" />
                          1. Solicitud de Adopción
                        </CardTitle>
                      </CardHeader>
                      <CardContent className="space-y-2">
                        <div className="flex items-center text-sm">
                          <Clock className="mr-2 h-4 w-4 text-gray-500" />
                          <span>15-20 minutos</span>
                        </div>
                        <Badge className="bg-yellow-100 text-yellow-800">PENDING</Badge>
                        <p className="text-sm text-gray-600">
                          Completa el formulario detallado con tu información personal, motivación, experiencia con
                          mascotas, y detalles de tu hogar.
                        </p>
                        <ul className="text-xs text-gray-500 space-y-1">
                          <li>• Información personal y contacto</li>
                          <li>• Motivación para adoptar</li>
                          <li>• Experiencia con mascotas</li>
                          <li>• Detalles del hogar y familia</li>
                        </ul>
                      </CardContent>
                    </Card>

                    <Card className="border-orange-200">
                      <CardHeader className="pb-3">
                        <CardTitle className="text-lg flex items-center text-orange-800">
                          <Stethoscope className="mr-2 h-5 w-5 text-green-600" />
                          2. Evaluación Veterinaria
                        </CardTitle>
                      </CardHeader>
                      <CardContent className="space-y-2">
                        <div className="flex items-center text-sm">
                          <Clock className="mr-2 h-4 w-4 text-gray-500" />
                          <span>2-3 días hábiles</span>
                        </div>
                        <Badge className="bg-blue-100 text-blue-800">EN REVISIÓN</Badge>
                        <p className="text-sm text-gray-600">
                          Nuestro equipo veterinario revisa tu solicitud y evalúa la compatibilidad entre tú y la
                          mascota seleccionada.
                        </p>
                        <ul className="text-xs text-gray-500 space-y-1">
                          <li>• Análisis de compatibilidad</li>
                          <li>• Evaluación de experiencia</li>
                          <li>• Verificación de referencias</li>
                          <li>• Contacto telefónico inicial</li>
                        </ul>
                      </CardContent>
                    </Card>

                    <Card className="border-orange-200">
                      <CardHeader className="pb-3">
                        <CardTitle className="text-lg flex items-center text-orange-800">
                          <Home className="mr-2 h-5 w-5 text-purple-600" />
                          3. Visita Domiciliaria
                        </CardTitle>
                      </CardHeader>
                      <CardContent className="space-y-2">
                        <div className="flex items-center text-sm">
                          <Clock className="mr-2 h-4 w-4 text-gray-500" />
                          <span>45-60 minutos</span>
                        </div>
                        <Badge className="bg-purple-100 text-purple-800">PROGRAMADA</Badge>
                        <p className="text-sm text-gray-600">
                          Un miembro de nuestro equipo visitará tu hogar para verificar las condiciones y conocer a tu
                          familia.
                        </p>
                        <ul className="text-xs text-gray-500 space-y-1">
                          <li>• Verificación del espacio</li>
                          <li>• Evaluación de seguridad</li>
                          <li>• Conocer a la familia</li>
                          <li>• Recomendaciones específicas</li>
                        </ul>
                      </CardContent>
                    </Card>

                    <Card className="border-orange-200">
                      <CardHeader className="pb-3">
                        <CardTitle className="text-lg flex items-center text-orange-800">
                          <CheckCircle className="mr-2 h-5 w-5 text-green-600" />
                          4. Adopción Final
                        </CardTitle>
                      </CardHeader>
                      <CardContent className="space-y-2">
                        <div className="flex items-center text-sm">
                          <Clock className="mr-2 h-4 w-4 text-gray-500" />
                          <span>1-2 horas</span>
                        </div>
                        <Badge className="bg-green-100 text-green-800">APROBADA</Badge>
                        <p className="text-sm text-gray-600">
                          Firma del contrato de adopción, entrega oficial de tu nueva mascota y kit de bienvenida
                          completo.
                        </p>
                        <ul className="text-xs text-gray-500 space-y-1">
                          <li>• Firma de contrato</li>
                          <li>• Entrega de documentos</li>
                          <li>• Kit de bienvenida</li>
                          <li>• Instrucciones de cuidado</li>
                        </ul>
                      </CardContent>
                    </Card>
                  </div>

                  {/* Seguimiento Post-Adopción */}
                  <Card className="border-green-200 bg-green-50">
                    <CardHeader>
                      <CardTitle className="text-lg flex items-center text-green-800">
                        <Calendar className="mr-2 h-5 w-5" />
                        Seguimiento Post-Adopción
                      </CardTitle>
                    </CardHeader>
                    <CardContent>
                      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                        <div className="text-center">
                          <div className="bg-green-100 w-12 h-12 rounded-full flex items-center justify-center mx-auto mb-2">
                            <span className="text-green-600 font-bold">3M</span>
                          </div>
                          <h4 className="font-semibold text-green-800">3 Meses</h4>
                          <p className="text-sm text-green-600">Primera evaluación de adaptación</p>
                        </div>
                        <div className="text-center">
                          <div className="bg-green-100 w-12 h-12 rounded-full flex items-center justify-center mx-auto mb-2">
                            <span className="text-green-600 font-bold">6M</span>
                          </div>
                          <h4 className="font-semibold text-green-800">6 Meses</h4>
                          <p className="text-sm text-green-600">Evaluación de bienestar y salud</p>
                        </div>
                        <div className="text-center">
                          <div className="bg-green-100 w-12 h-12 rounded-full flex items-center justify-center mx-auto mb-2">
                            <span className="text-green-600 font-bold">12M</span>
                          </div>
                          <h4 className="font-semibold text-green-800">12 Meses</h4>
                          <p className="text-sm text-green-600">Evaluación final de adopción exitosa</p>
                        </div>
                      </div>
                    </CardContent>
                  </Card>

                  {/* Requisitos y Compromisos */}
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <Card className="border-blue-200">
                      <CardHeader>
                        <CardTitle className="text-lg text-blue-800">📋 Requisitos Importantes</CardTitle>
                      </CardHeader>
                      <CardContent>
                        <ul className="space-y-2 text-sm">
                          <li className="flex items-center">
                            <CheckCircle className="mr-2 h-4 w-4 text-green-500" />
                            Ser mayor de edad con documento
                          </li>
                          <li className="flex items-center">
                            <CheckCircle className="mr-2 h-4 w-4 text-green-500" />
                            Espacio adecuado para la mascota
                          </li>
                          <li className="flex items-center">
                            <CheckCircle className="mr-2 h-4 w-4 text-green-500" />
                            Estabilidad económica para gastos veterinarios
                          </li>
                          <li className="flex items-center">
                            <CheckCircle className="mr-2 h-4 w-4 text-green-500" />
                            Compromiso de cuidado de por vida
                          </li>
                          <li className="flex items-center">
                            <CheckCircle className="mr-2 h-4 w-4 text-green-500" />
                            Aceptar visitas de seguimiento
                          </li>
                        </ul>
                      </CardContent>
                    </Card>

                    <Card className="border-red-200">
                      <CardHeader>
                        <CardTitle className="text-lg text-red-800">❤️ Compromisos del Adoptante</CardTitle>
                      </CardHeader>
                      <CardContent>
                        <ul className="space-y-2 text-sm">
                          <li className="flex items-center">
                            <Heart className="mr-2 h-4 w-4 text-red-500" />
                            Proporcionar amor y cuidado diario
                          </li>
                          <li className="flex items-center">
                            <Stethoscope className="mr-2 h-4 w-4 text-red-500" />
                            Mantener vacunas y cuidados veterinarios
                          </li>
                          <li className="flex items-center">
                            <Shield className="mr-2 h-4 w-4 text-red-500" />
                            Nunca abandonar o maltratar
                          </li>
                          <li className="flex items-center">
                            <Phone className="mr-2 h-4 w-4 text-red-500" />
                            Contactar refugio ante problemas
                          </li>
                          <li className="flex items-center">
                            <Home className="mr-2 h-4 w-4 text-red-500" />
                            Devolver al refugio si no puede continuar
                          </li>
                        </ul>
                      </CardContent>
                    </Card>
                  </div>

                  {/* Información de Contacto */}
                  <Card className="border-orange-200 bg-orange-50">
                    <CardHeader>
                      <CardTitle className="text-lg text-orange-800">📞 ¿Tienes Preguntas?</CardTitle>
                    </CardHeader>
                    <CardContent>
                      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                          <h4 className="font-semibold text-orange-800 mb-2">Contacto General</h4>
                          <div className="space-y-1 text-sm">
                            <div className="flex items-center">
                              <Phone className="mr-2 h-4 w-4 text-orange-600" />
                              +57 300 123 4567
                            </div>
                            <div className="flex items-center">
                              <Mail className="mr-2 h-4 w-4 text-orange-600" />
                              adopciones@avesdehermes.com
                            </div>
                          </div>
                        </div>
                        <div>
                          <h4 className="font-semibold text-orange-800 mb-2">Horarios de Atención</h4>
                          <div className="text-sm text-orange-600">
                            <p>Lunes a Viernes: 8:00 AM - 6:00 PM</p>
                            <p>Sábados: 9:00 AM - 4:00 PM</p>
                            <p>Domingos: 10:00 AM - 2:00 PM</p>
                          </div>
                        </div>
                      </div>
                    </CardContent>
                  </Card>

                  {/* Botones de Acción */}
                  <div className="flex flex-col sm:flex-row gap-4 justify-center pt-4">
                    <Button className="bg-green-600 hover:bg-green-700 text-white" asChild>
                      <Link href="/register">
                        <Users className="mr-2 h-4 w-4" />
                        Registrarse como Adoptante
                      </Link>
                    </Button>
                    <Button
                      variant="outline"
                      className="border-orange-600 text-orange-600 hover:bg-orange-50 bg-transparent"
                      onClick={() => setShowAdoptionProcess(false)}
                    >
                      Cerrar
                    </Button>
                  </div>
                </div>
              </DialogContent>
            </Dialog>
            <Button
              size="lg"
              variant="outline"
              className="border-orange-600 text-orange-600 hover:bg-orange-50 px-8 py-3 shadow-lg hover:shadow-xl transition-all duration-300 transform hover:-translate-y-1 bg-transparent"
              asChild
            >
              <Link href="/shelter">
                <MapPin className="mr-2 h-5 w-5" />
                Conocer Refugios
              </Link>
            </Button>
          </div>
        </div>
      </section>

      {/* Stats Section */}
      <section className="py-16 bg-white/70 backdrop-blur-sm">
        <div className="container mx-auto px-4">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div className="text-center group hover:transform hover:scale-105 transition-all duration-300">
              <div className="bg-orange-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4 group-hover:bg-orange-200 transition-colors">
                <Heart className="h-8 w-8 text-orange-600" />
              </div>
              <div className="text-4xl font-bold text-orange-600 mb-2">500+</div>
              <div className="text-orange-800 font-medium">Mascotas Adoptadas</div>
            </div>
            <div className="text-center group hover:transform hover:scale-105 transition-all duration-300">
              <div className="bg-green-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4 group-hover:bg-green-200 transition-colors">
                <Users className="h-8 w-8 text-green-600" />
              </div>
              <div className="text-4xl font-bold text-green-600 mb-2">25+</div>
              <div className="text-orange-800 font-medium">Refugios Aliados</div>
            </div>
            <div className="text-center group hover:transform hover:scale-105 transition-all duration-300">
              <div className="bg-amber-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4 group-hover:bg-amber-200 transition-colors">
                <Award className="h-8 w-8 text-amber-600" />
              </div>
              <div className="text-4xl font-bold text-amber-600 mb-2">1000+</div>
              <div className="text-orange-800 font-medium">Familias Felices</div>
            </div>
          </div>
        </div>
      </section>

      {/* Main Content Tabs */}
      <section id="mascotas" className="py-16 px-4">
        <div className="container mx-auto">
          <Tabs value={activeTab} onValueChange={setActiveTab} className="w-full">
            <TabsList className="grid w-full grid-cols-2 max-w-md mx-auto mb-12 bg-orange-100/80 backdrop-blur-sm shadow-lg">
              <TabsTrigger
                value="pets"
                className="data-[state=active]:bg-orange-600 data-[state=active]:text-white data-[state=active]:shadow-lg transition-all duration-300 flex items-center"
              >
                <PawPrint className="h-4 w-4 mr-2" />
                Mascotas Disponibles
              </TabsTrigger>
              <TabsTrigger
                value="shelters"
                className="data-[state=active]:bg-orange-600 data-[state=active]:text-white data-[state=active]:shadow-lg transition-all duration-300 flex items-center"
              >
                <MapPin className="h-4 w-4 mr-2" />
                Refugios
              </TabsTrigger>
            </TabsList>

            <TabsContent value="pets" className="space-y-8">
              <div className="text-center mb-12">
                <h3 className="text-3xl font-bold text-orange-800 mb-4">Mascotas Esperando un Hogar</h3>
                <p className="text-orange-700 max-w-2xl mx-auto">
                  Cada una de estas mascotas tiene una historia única y está lista para comenzar una nueva vida contigo.
                </p>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                {mockPets.map((pet) => (
                  <Card
                    key={pet.id}
                    className="overflow-hidden hover:shadow-2xl transition-all duration-300 border-orange-200 group hover:border-orange-300 hover:-translate-y-1"
                  >
                    <div className="relative overflow-hidden">
                      <Image
                        src={pet.image || "/placeholder.svg"}
                        alt={pet.name}
                        width={400}
                        height={300}
                        className="w-full h-64 object-cover group-hover:scale-110 transition-transform duration-500"
                      />
                      <div className="absolute inset-0 bg-gradient-to-t from-black/20 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>
                      <Badge className="absolute top-4 right-4 bg-green-600 text-white shadow-lg">
                        <Heart className="h-3 w-3 mr-1" />
                        Disponible
                      </Badge>
                    </div>
                    <CardHeader>
                      <div className="flex justify-between items-start">
                        <div>
                          <CardTitle className="text-orange-800">{pet.name}</CardTitle>
                          <CardDescription className="text-orange-600">
                            {pet.breed} • {getSizeLabel(pet.size)} • {getGenderLabel(pet.gender)}
                          </CardDescription>
                        </div>
                        <div className="text-right text-sm text-orange-600">
                          <div className="flex items-center">
                            <Calendar className="h-4 w-4 mr-1" />
                            {calculateAge(pet.birthDate)}
                          </div>
                        </div>
                      </div>
                    </CardHeader>
                    <CardContent className="space-y-4">
                      <div className="flex items-start space-x-2">
                        <Users className="h-4 w-4 text-orange-600 mt-1 flex-shrink-0" />
                        <p className="text-sm text-gray-600">{pet.behaviorProfile}</p>
                      </div>
                      <div className="flex items-center justify-between text-sm">
                        <div className="flex items-center text-orange-600">
                          <MapPin className="h-4 w-4 mr-1" />
                          {pet.shelter.name}
                        </div>
                        <div className="flex items-center text-green-600">
                          <Shield className="h-4 w-4 mr-1" />
                          Vacunado
                        </div>
                      </div>
                      <Button
                        className="w-full bg-orange-600 hover:bg-orange-700 text-white group hover:shadow-lg transition-all duration-300"
                        asChild
                      >
                        <Link href={`/pet/${pet.id}`}>
                          <Heart className="mr-2 h-4 w-4 group-hover:scale-110 transition-transform" />
                          Conocer a {pet.name}
                        </Link>
                      </Button>
                    </CardContent>
                  </Card>
                ))}
              </div>
            </TabsContent>

            <TabsContent value="shelters" className="space-y-8">
              <div className="text-center mb-12">
                <h3 className="text-3xl font-bold text-orange-800 mb-4">Refugios Aliados</h3>
                <p className="text-orange-700 max-w-2xl mx-auto">
                  Conoce los refugios que trabajan incansablemente para cuidar y encontrar hogares para las mascotas.
                </p>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                {mockShelters.map((shelter) => (
                  <Card
                    key={shelter.id}
                    className="hover:shadow-xl transition-all duration-300 border-orange-200 group hover:border-orange-300 overflow-hidden"
                  >
                    <div className="relative">
                      <Image
                        src={shelter.image || "/placeholder.svg"}
                        alt={shelter.name}
                        width={300}
                        height={200}
                        className="w-full h-48 object-cover group-hover:scale-105 transition-transform duration-300"
                      />
                      <div className="absolute top-4 left-4 bg-white/90 backdrop-blur-sm rounded-full p-2">
                        <Users className="h-5 w-5 text-orange-600" />
                      </div>
                    </div>
                    <CardHeader>
                      <div className="flex items-start justify-between">
                        <div>
                          <CardTitle className="text-orange-800 flex items-center">
                            <Award className="h-5 w-5 mr-2 text-orange-600" />
                            {shelter.name}
                          </CardTitle>
                          <CardDescription className="text-orange-600 mt-2">{shelter.description}</CardDescription>
                        </div>
                      </div>
                    </CardHeader>
                    <CardContent className="space-y-3">
                      <div className="flex items-center text-sm text-gray-600 hover:text-orange-600 transition-colors">
                        <MapPin className="h-4 w-4 mr-2 text-orange-600" />
                        {shelter.address}
                      </div>
                      <div className="flex items-center text-sm text-gray-600 hover:text-orange-600 transition-colors">
                        <Phone className="h-4 w-4 mr-2 text-orange-600" />
                        {shelter.phone}
                      </div>
                      <div className="flex items-center text-sm text-gray-600 hover:text-orange-600 transition-colors">
                        <Mail className="h-4 w-4 mr-2 text-orange-600" />
                        {shelter.email}
                      </div>
                      <div className="bg-orange-50 p-3 rounded-lg">
                        <div className="flex items-center text-sm font-medium text-orange-600">
                          <PawPrint className="h-4 w-4 mr-2" />
                          {shelter.petsCount} mascotas en cuidado
                        </div>
                      </div>
                      <Button
                        className="w-full bg-green-600 hover:bg-green-700 text-white group hover:shadow-lg transition-all duration-300"
                        asChild
                      >
                        <Link href={`/shelter/${shelter.id}`}>
                          <MapPin className="mr-2 h-4 w-4 group-hover:scale-110 transition-transform" />
                          Visitar Refugio
                        </Link>
                      </Button>
                    </CardContent>
                  </Card>
                ))}
              </div>
            </TabsContent>
          </Tabs>
        </div>
      </section>

      {/* Success Stories */}
      <section id="historias" className="py-16 bg-gradient-to-br from-white/70 to-orange-50/70 backdrop-blur-sm">
        <div className="container mx-auto px-4">
          <div className="text-center mb-12">
            <div className="flex justify-center mb-6">
              <div className="bg-orange-100 p-4 rounded-full">
                <Award className="h-8 w-8 text-orange-600" />
              </div>
            </div>
            <h3 className="text-3xl font-bold text-orange-800 mb-4">Historias de Éxito</h3>
            <p className="text-orange-700 max-w-2xl mx-auto">
              Estas son algunas de las hermosas historias de adopción que nos llenan de alegría y motivación.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {successStories.map((story) => (
              <Card
                key={story.id}
                className="text-center border-orange-200 hover:shadow-xl transition-all duration-300 group hover:border-orange-300"
              >
                <CardHeader>
                  <div className="mx-auto mb-4 relative">
                    <div className="absolute -inset-2 bg-gradient-to-r from-orange-400 to-amber-400 rounded-full opacity-20 group-hover:opacity-30 transition-opacity"></div>
                    <Image
                      src={story.image || "/placeholder.svg"}
                      alt={story.petName}
                      width={150}
                      height={150}
                      className="rounded-full mx-auto relative z-10 group-hover:scale-105 transition-transform duration-300"
                    />
                  </div>
                  <CardTitle className="text-orange-800 flex items-center justify-center">
                    <Heart className="h-5 w-5 mr-2 text-red-500" />
                    {story.petName}
                  </CardTitle>
                  <CardDescription className="text-orange-600 flex items-center justify-center">
                    <Users className="h-4 w-4 mr-1" />
                    Adoptado por {story.ownerName}
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  <p className="text-gray-600 italic mb-4">"{story.story}"</p>
                  <div className="flex items-center justify-center text-sm text-orange-600 bg-orange-50 p-2 rounded-lg">
                    <Calendar className="h-4 w-4 mr-1" />
                    {story.months} meses de felicidad
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-20 bg-gradient-to-r from-orange-600 via-amber-600 to-orange-700 text-white relative overflow-hidden">
        <div className="absolute inset-0 bg-black/10"></div>
        <div className="absolute top-10 left-10 text-white/20">
          <PawPrint className="h-24 w-24" />
        </div>
        <div className="absolute bottom-10 right-10 text-white/20">
          <Heart className="h-20 w-20" />
        </div>
        <div className="container mx-auto px-4 text-center relative z-10">
          <div className="flex justify-center mb-6">
            <div className="bg-white/20 p-4 rounded-full backdrop-blur-sm">
              <Users className="h-12 w-12 text-white" />
            </div>
          </div>
          <h3 className="text-4xl font-bold mb-6">¿Listo para cambiar una vida?</h3>
          <p className="text-xl mb-8 max-w-2xl mx-auto opacity-90 leading-relaxed">
            Únete a nuestra comunidad de adoptantes y brinda amor a una mascota que lo necesita. El proceso es simple y
            te acompañamos en cada paso.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Button
              size="lg"
              className="bg-white text-orange-600 hover:bg-gray-100 px-8 py-3 shadow-xl hover:shadow-2xl transition-all duration-300 transform hover:-translate-y-1"
            >
              <Heart className="mr-2 h-5 w-5" />
              Registrarse como Adoptante
            </Button>
            <Button
              size="lg"
              variant="outline"
              className="border-white text-white hover:bg-white/10 px-8 py-3 shadow-xl hover:shadow-2xl transition-all duration-300 transform hover:-translate-y-1 backdrop-blur-sm bg-transparent"
            >
              <Award className="mr-2 h-5 w-5" />
              Conocer el Proceso
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
                  <Link href="#" className="hover:text-white">
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
                  <Link href="#" className="hover:text-white">
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
                <li>info@Sistema de Veterinaria.com</li>
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
