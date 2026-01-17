# Sistema de Gestión de Almacenes (WMS) — Trabajo Práctico Integrador Programación II
**Tecnicatura Universitaria en Programación**  
**Programación II**  
**Año 2025**

## 🚀 Cómo Levantar el Proyecto

### Requisitos Previos
- Java JDK 21 o superior instalado
- Verificar instalación: `java -version` y `javac -version`

### ⚡ Ejecución Rápida (Recomendada)

Los scripts compilan automáticamente y ejecutan el programa:

1. **Ejecutar en modo GUI (interfaz gráfica):**
   ```bash
   ejecutar.bat
   ```

2. **Ejecutar en modo Consola:**
   ```bash
   ejecutar-consola.bat
   ```

Los scripts crean automáticamente la carpeta `bin/` si no existe y compilan los archivos allí.

### 📝 Compilación Manual (Opcional)

Si prefieres compilar manualmente antes de ejecutar:

```bash
compilar.bat
```

Luego ejecutar:
```bash
java -cp bin Main          # Modo GUI
java -cp bin Main consola  # Modo Consola
```

### 🧹 Limpiar archivos compilados

Para eliminar todos los archivos compilados:
```bash
limpiar-compilados.bat
```

Esto eliminará la carpeta `bin/` y cualquier archivo `.class` en `src/`.

**Nota:** Los archivos `.class` se generan en la carpeta `bin/`, separados del código fuente en `src/`.

### 🔐 Sistema de Autenticación
Al iniciar el programa, se solicitará autenticación:

**Usuarios disponibles:**
- **Admin** (acceso completo - CRUD):
  - Usuario: `admin`
  - Contraseña: `admin123`
  
- **User** (solo lectura):
  - Usuario: `user`
  - Contraseña: `user123`

**Nota:** Los usuarios con rol `USER` solo pueden consultar información. Los usuarios `ADMIN` tienen permisos completos para crear, modificar y eliminar.

## Arquitectura del Proyecto
El proyecto está organizado en base al patrón de diseño MVC (Modelo-Vista-Controlador), DAO para persistencia en archivos .csv y services para la lógica del programa.

#### Diagrama UML del modelo
![diamgramaUML](/uml-model-2.png)

## ESTRUCTURA
| Capa| Rol|
| --------------    | -------------------|
| **Model**         | Entidades
| **Services**      | Lógica de negocio
| **DAO**           | Lee/guarda en los `.csv`
| **Controller**    | Intermediario entre view y services                                     
| **View**          | Interacción con el usuario                              
| **data**          | Archivos `.csv`                                  

## CLASES
### Model
- Producto
- StockUbicacion (intermedia Producto - Ubicacion)
- Ubicacion
- Rack
- Nave
- OrdenMovimiento
- OrdenTransformacion
- MovimientoTrazabilidad
- TipoMovimiento (enum)
- UnidadMedida (enum)
- **User** (nuevo - sistema de autenticación)
- **Role** (enum - ADMIN, USER)
### Services
- MovimentoService
- NaveService
- RackService
- StockService
- TransformacionService
- **AuthService** (nuevo - autenticación y hash de contraseñas)
- **PermissionService** (nuevo - control de permisos)
### DAO
- NaveDAO
- OrdenMovDAO
- OrdenTransfDAO
- ProductoDAO
- RackDAO
- StockDAO
### Controller
- ConsultasController
- HistorialController
- MovimientoController
- NaveController
- ProductoController
- TransformacionController
### View
- ConsultasView
- HistorialView
- NaveView
- OrdenesMovView
- ProductoView
- TransformacionView
- View (clase abstracta)

## ARCHIVOS CSV
### Naves:
|naves.csv | idNave |
| --------- |  --------- |
| **TIPO**  |`int` |
| **EJEMPLO**  |`1` |
``` 
Se guarda el ID de cada nave creada
```
---
### Racks:
|racks.csv | idNave |idRack|
| --------- |  --------- |--------- |
| **TIPO**  |`int` |`int`|
| **EJEMPLO**  |`1` | `1`| 
```
Se guarda el ID de la nave a la que pertenece junto con el ID del rack
```
---
### Productos:
|productos.csv | idProducto | descripcion | UnidadMedida | Peso unitario(kg) | Capacidad contenedor | Stock minimo | Grupo | Código
| --------- |  --------- |  --------- |-------- |-------- |-------- |-------- |-------- |-------- |
| **TIPO**  |`int` |`String` |`enum`|`double`| `double`| `int`| `String`| `String`|
| **EJEMPLO**  |`1` |`Yerba Union 500gr` |`GRAMOS` |`0,5`|`500`|`200`|`Producto final`| `PF-01`|

El peso unitario se debe ingresar en kg para llevar control del peso máximo por ubicación. La capacidad del contenedor se mide en la unidad de medida ingresada para el producto y es lo que cambia al realizar una transformación. El grupo organiza los productos en "Materia prima", "Producto final" y "Producto reenvazado". El código del producto es una abreviación del grupo y su id.

---
### Stock:
|stock.csv | idProducto | codigoUbicacion |cantidad |
| --------- |  --------- |  --------- |--------- |
| **TIPO**  |`int` |`String` |`int` |
| **EJEMPLO**  |`1` |`1-1-1-2` |`10` |
```
El código de ubicacion se refiere a 1 (Nave 1) - 1 (Rack 1) - fila 1 - columna 2
```
---
### Ordenes de movimiento:
|movimiento.csv | tipo | idMov | cantidad | idProducto | fecha |ubicacion |ubicacionOrigen |ubicacionDestino | usuarioResponsable |
| --- |  ---|  --- |--- |--- |--- |---|--- |--- |--- |
| **TIPO**  |`TipoMovimiento` |`int` |`int` |`int` |`LocalDate` |`String` |`String` |`String` |`String` |
| **EJEMPLO INGRESO**  |`INGRESO` |`1`|`3`|`3`|`2025-10-21`|`R1-1-1`|`(vacio)`|`(vacio)`|`admin`|
| **EJEMPLO INTERNO**  |`INTERNO` |`2`|`2`|`1`|`2025-10-22`|`(vacio)`|`1-1-1-1`|`1-1-1-2`|`admin`|

Las órdenes de movimiento utilizan diferentes constructores dependiendo de su tipo. Pueden ser de movimiento interno, de ingreso o egreso. Las órdenes de movimiento interno usan en su constructor dos ubicaciones, origen y destino. Las órdenes de ingreso/egreso sólo usan una ubicacion: donde se asigna el producto al ingresarlo (en caso de tipo ingreso), o de donde se quitó al egresarlo (en caso de tipo egreso).

---
### Ordenes de transformación:
|transformaciones.csv | idOrdenTransf | idProdEntrada | ubicacionProdEntrada | cantEntrada | idProdTransformado | ubicacionProdSalida| cantidadSalida | fecha | usuarioResponsable |
| --------- |  --------- |  --------- |-------- |-------- |-------- |-------- |-------- |-------- |-------- |
| **TIPO**  |`int` |`int` |`String`|`int`| `int`| `String`| `int`| `LocalDate`| `String`|
| **EJEMPLO**  |`1` |`1` |`1-1-1-1` |`10`|`2`|`1-1-1-3`|`100`| `2025-11-08`| `admin`|


Las ordenes de transformación reenvasan los productos de tipo materia prima, cambiando la capacidad del contenedor.

---
## Funcionalidades Principales
- **Sistema de autenticación** con roles (ADMIN/USER) y control de permisos
- Crear y listar **naves**, **racks** y **ubicaciones**.
- Registrar un catálogo de **productos** con sus datos correspondientes.
- Realizar **movimientos de stock** (ingreso, egreso e interno).
- Registrar y consultar **órdenes de transformación** (reenvase).
- Consultar **historial de movimientos** y trazabilidad de productos.
- Verificar **stock disponible** y capacidad por ubicación.
- Realizar consultas varias.
- **Auditoría completa**: todas las operaciones registran el usuario responsable

## Ejemplo de Ejecución
Menú Principal
1. NAVES
2. PRODUCTOS
3. ORDENES DE MOVIMIENTO
4. ORDENES DE TRANSFORMACION
5. HISTORIAL DE MOVIMIENTOS
6. CONSULTAS
0. Salir
-----------------------------
>Selección: 1

Menú Naves
1. Ver naves
2. Crear nave
3. Crear rack en nave
4. Listar racks en nave
5. Listar ubicaciones disponibles de un rack
6. Mostrar peso de un rack
0. Volver
----------------------------
>Selección 2
```
Nave creada con ID 1
```
>Selección 3
```
Ingrese ID nave: 1
Rack de ID 1 creado con éxito.
```
Después de crear al menos una nave, al menos 1 rack y registrar al menos 1 producto, ya se puede comenzar a realizar cualquiera de las demás funcionalidades disponibles.
---
## Flujo del programa

INICIO

↓

Menú principal
1. NAVES → Menú Naves → Acciones de Naves
2. PRODUCTOS → Menú Productos → Acciones Productos
3. MOVIMIENTOS → Ingreso / Egreso / Traslado    
4. TRANSFORMACIONES → Registrar / Ver historial
5. HISTORIAL → Consultar movimientos y transformaciones
6. CONSULTAS → Reportes y búsquedas
   #### 0. SALIR → Fin del programa


    └── Tras finalizar cada submenú, vuelve al menú principal

↓

FIN

