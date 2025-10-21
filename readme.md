# Sistema de Gestión de Almacenes (WMS) — Trabajo Práctico Integrador Programación II
**Tecnicatura Universitaria en Programación**  
**Programación II**  
**Año 2025**  
**Lenguaje Java**  
**Participantes:** 


## Arquitectura del Proyecto
El proyecto está organizado en base al patrón de diseño MVC (Modelo-Vista-Controlador), repositories para persistencia en archivos .csv y services para la lógica del programa.

#### Diagrama UML del modelo
![diamgramaUML](/uml%20model.png)

## ESTRUCTURA
| Capa| Rol|
| --------------    | -------------------|
| **Model**         | Entidades
| **Services**      | Lógica de negocio
| **Repositories**  | Lee/guarda en los `.csv`
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
### Repositories
- StockRepository
- OrdenMoviRepository
### Services
- MovimentoService
- ManejadorStock
### Controller
`por hacer`
### View
`por hacer`

## ARCHIVOS CSV
|stock.csv | idProducto | codigoUbicacion |cantidad |
| --------- |  --------- |  --------- |--------- |
| **TIPO**  |`int` |`String` |`int` |
| **EJEMPLO**  |`1` |`R1-1-2` |`10` |
```
El código de ubicacion se refiere a R1 (Rack 1) - fila 1 - columna 2
```
---
|movimiento.csv | tipo | idMov | cantidad | idProducto | fecha |ubicacion |ubicacionOrigen |ubicacionDestino |
| --- |  ---|  --- |--- |--- |--- |---|--- |--- |
| **TIPO**  |`TipoMovimiento` |`int` |`int` |`int` |`LocalDate` |`String` |`String` |`String` |
| **EJEMPLO INGRESO**  |`INGRESO` |`1`|`3`|`3`|`2025-10-21`|`R1-1-1`|`(vacio)`|`(vacio)`|
| **EJEMPLO INTERNO**  |`INTERNO` |`2`|`2`|`1`|`2025-10-22`|`(vacio)`|`R1-1-1`|`R1-1-2`|
```
Las órdenes de movimiento utilizan diferentes constructores dependiendo de su tipo, que puede ser interno, de ingreso o egreso. Las órdenes de mov. interno usan dos ubicaciones, origen y destino. Las órdenes de ingreso/egreso sólo usan una ubicacion, donde se asigna el producto al ingresarlo, y de dónde se quitó para egresarlo.
```
---


