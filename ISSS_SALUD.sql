------ TABLA DE ROLES -------

CREATE TABLE Roles(
id_rol INT PRIMARY KEY,
nombre_rol VARCHAR2(50) NOT NULL,
descripcion_rol VARCHAR2(250) NOT NULL
);

------ SECUENCIA PARA LA TABLA DE ROLES --------

CREATE SEQUENCE sec_rol
START WITH 1
INCREMENT BY 1;

------- TRIGGER IMPLEMENTADO EN LA TABLA DE ROLES (GENERADOR DE IDS) --------

CREATE TRIGGER TrigRol
BEFORE INSERT ON Roles
FOR EACH ROW
BEGIN
SELECT sec_rol.NEXTVAL INTO: NEW.id_rol
FROM DUAL;
END;

-------- INSERTS EN LA TABLA DE ROLES ------------

INSERT ALL
INTO Roles (nombre_rol, descripcion_rol) VALUES ('Administrador', 'Tiene la capacidad de supervisar toda la cuenta; gestionando todo, desde usuarios y tableros hasta seguridad y facturación')
INTO Roles (nombre_rol, descripcion_rol) VALUES ('Paciente', 'Persona que es atendida por un profesional de la salud debido a un problema de salud')
INTO Roles (nombre_rol, descripcion_rol) VALUES ('Jefes de enfermería', 'Encargados de agendar citas y modificarlas, pueden mandar la asignación de cita a los doctores designados y el recordatorio programado a los usuarios')
SELECT * FROM dual;

------ SELECT DEL ESTADO DE INSERTS DE LA TABLA ------

SELECT * FROM Roles;

------ TABLA DE USUARIOS ---------

CREATE TABLE Usuarios(
id_usuario INT PRIMARY KEY,
dui VARCHAR2(10) UNIQUE NOT NULL,
tipo_sangre VARCHAR2(5) NOT NULL,
telefono VARCHAR2(9)UNIQUE NOT NULL,
foto_usuario VARCHAR2(255) NULL,
correo_electronico VARCHAR2(100) UNIQUE NOT NULL,
contrasena VARCHAR2(255) NOT NULL,
id_rol INT,
CONSTRAINT fk_rol
FOREIGN KEY (id_rol) REFERENCES Roles(id_rol)
);

------ TABLA DE AUDITORIA PARA EL UPDATE DE CAMPOS DE LA TABLA DE USUARIOS -------

SELECT * FROM Doctores;

CREATE TABLE AuditoriaUsuarios(
    id_auditoria INT PRIMARY KEY,
    correo_electronico VARCHAR2(100) NOT NULL,
    fecha_operacion DATE NOT NULL,
    campo_modificado VARCHAR2(50) NOT NULL,
    valor_anterior VARCHAR2(255) NULL,
    valor_nuevo VARCHAR2(255) NULL
);

------- PROCEDIMIENTO ALMACENADO DE LLENADO DE LA TABLA DE AUDITORIA DEPENDIENDO DE LOS UPDATES REALIZADOS EN LA TABLA DE USUARIOS -----

CREATE OR REPLACE PROCEDURE AuditarUsuario(
    p_correo_electronico VARCHAR2,
    p_campo_modificado VARCHAR2,
    p_valor_anterior VARCHAR2,
    p_valor_nuevo VARCHAR2
) AS
BEGIN
    INSERT INTO AuditoriaUsuarios (id_auditoria, correo_electronico, fecha_operacion, campo_modificado, valor_anterior, valor_nuevo)
    VALUES (sec_auditoria_usuarios.NEXTVAL, p_correo_electronico, SYSDATE, p_campo_modificado, p_valor_anterior, p_valor_nuevo);
END AuditarUsuario;
/

------ DECALARACIÓN DE LOS NUEVOS VALORES PARA LOS CAMPOS DE LA TABLA USUARIOS -------

DECLARE
    v_correo_electronico VARCHAR2(100) := 'luca@gmail.com';
    v_telefono_antiguo VARCHAR2(10);
    v_telefono_nuevo VARCHAR2(10) := '6535-4787';
BEGIN
    SELECT telefono INTO v_telefono_antiguo FROM Usuarios WHERE correo_electronico = v_correo_electronico;

    UPDATE Usuarios
    SET telefono = v_telefono_nuevo
    WHERE correo_electronico = v_correo_electronico;

    AuditarUsuario(v_correo_electronico, 'telefono', v_telefono_antiguo, v_telefono_nuevo);
END;
/

----- SECUENCIA PARA LA TABLA DE AUDITORIA DE USUARIOS ------

CREATE SEQUENCE sec_auditoria_usuarios
START WITH 1
INCREMENT BY 1;

------ SELECT SEGÚN EL ORDEN DE CREACIÓN DE USERS EN LA TABLA USUARIOS ------

SELECT *
FROM Usuarios
ORDER BY id_usuario ASC;

------ SELECT DE VALIDACIÓN DE USUARIOS EXISTENTES ------

SELECT COUNT(*) FROM Usuarios WHERE correo_electronico = 'luca@gmail.com' AND contrasena = '4006b8cd3afa8484856cc1917d838861d9796fe413c1d1ea55af4260bfbd1775';

----- SELECT PARA VERIFICAR EL ESTADO DE MODIFICACIONES DE LA TABLA USUARIOS A TRAVÉS DE LA TABLA DE AUDITORIA DE USUARIOS -----

SELECT * FROM AuditoriaUsuarios;

---- SECUENCIA PARA LA TABLA DE USUARIOS -----

CREATE SEQUENCE sec_usuarios
START WITH 1
INCREMENT BY 1;

----- TRIGGER PARA LA CREACIÓN DE IDS PARA LA TABLA DE USUARIO -------

CREATE TRIGGER TrigUsuarios
BEFORE INSERT ON Usuarios
FOR EACH ROW
BEGIN
SELECT sec_usuarios.NEXTVAL INTO : NEW.id_usuario
FROM DUAL;
END;

------ INSERTS PARA LA TABLA DE USUARIOS --------

INSERT ALL
INTO Usuarios (dui, tipo_sangre, telefono, foto_usuario, correo_electronico, contrasena, id_rol) VALUES ('12345678-9', 'ORH+', '1234-5678', 'https://i.pinimg.com/564x/68/6e/b4/686eb4dc1be6b467189a666f91489c38.jpg', 'diana@gmail.com', 'Diana#06', 2)
INTO Usuarios (dui, tipo_sangre, telefono, foto_usuario, correo_electronico, contrasena, id_rol) VALUES ('98765432-1', 'AB+', '9874-5682', 'https://e0.pxfuel.com/wallpapers/898/129/desktop-wallpaper-cool-boy-boy-pic.jpg', 'arriaza@gmail.com', 'Arriaza$58', 3)
INTO Usuarios (dui, tipo_sangre, telefono, foto_usuario, correo_electronico, contrasena, id_rol) VALUES ('56975862-5', 'AB-', '5697-2145', 'https://s3-alpha.figma.com/hub/file/5059055856/ff93c76b-111f-4568-a67c-cfbd7036f020-cover.png', 'ricardo@gmail.com', 'Ricky100%', 2)
INTO Usuarios (dui, tipo_sangre, telefono, foto_usuario, correo_electronico, contrasena, id_rol) VALUES ('59215565-7', 'O+', '5632-4785', 'https://www.hartz.com/wp-content/uploads/2022/04/small-dog-owners-1.jpg', 'kevin@gmail.com', 'KevinAalvarado!', 3)
INTO Usuarios (dui, tipo_sangre, telefono, foto_usuario, correo_electronico, contrasena, id_rol) VALUES ('25634861-4', 'AB+', '5632-5214', 'https://cdn.pixabay.com/photo/2024/02/28/07/42/european-shorthair-8601492_640.jpg', 'rauda@gmail.com', 'is#JustSammy', 2)
INTO Usuarios (dui, tipo_sangre, telefono, foto_usuario, correo_electronico, contrasena, id_rol) VALUES ('56546545-3', 'AB-', '2356-1495', 'https://media.sproutsocial.com/uploads/2022/06/profile-picture.jpeg', 'luis@gmail.com', 'escalante$#4', 3)
INTO Usuarios (dui, tipo_sangre, telefono, foto_usuario, correo_electronico, contrasena, id_rol) VALUES ('16464646-6', 'O+', '4645-6576', 'https://sarahclaysocial.com/wp-content/uploads/2020/10/sarah-clay-3.jpg', 'sherry@gmail.com', 'roy#1997', 1)
SELECT * FROM dual;

---- SELECT PARA VERIFICAR EL ESTADO DE INSERTS DE LA TABLA DE USUARIOS ------

SELECT * FROM Usuarios;

------- TABLA DE ESPECIALDAD DE DOCTORES -------

CREATE TABLE EspecialidadDoctores(
id_especialidad INT PRIMARY KEY,
especialidad_doctor VARCHAR2(100) NOT NULL,
descripcion_especialidad VARCHAR2(200) NOT NULL
);

------ SECUENCIA PARA LA TABLA DE ESPECIALIDAD DOCTORES -------

CREATE SEQUENCE sec_especialidad_doctores 
START WITH 1
INCREMENT BY 1;

------- TRIGGER PARA LA CREACIÓN DE IDS PARA LA TABLA DE ESPECIALIDAD DOCTORES -------

CREATE TRIGGER TrigEspecialidadDoctores
BEFORE INSERT ON EspecialidadDoctores
FOR EACH ROW
BEGIN
SELECT sec_especialidad_doctores.NEXTVAL INTO : NEW.id_especialidad
FROM DUAL;
END;

---- INSERTS PARA LA TABLA DE ESPECIALIDAD DOCTORES -------

INSERT ALL
INTO EspecialidadDoctores (especialidad_doctor, descripcion_especialidad) VALUES ('Cardiología', 'Especialidad médica que se ocupa del diagnóstico y tratamiento de las enfermedades del corazón.')
INTO EspecialidadDoctores (especialidad_doctor, descripcion_especialidad) VALUES ('Neurología', 'Especialidad médica que se ocupa del estudio y tratamiento de las enfermedades del sistema nervioso.')
INTO EspecialidadDoctores (especialidad_doctor, descripcion_especialidad) VALUES ('Pediatría', 'Rama de la medicina que se ocupa del desarrollo y las enfermedades de los niños.')
INTO EspecialidadDoctores (especialidad_doctor, descripcion_especialidad) VALUES ('Dermatología', 'Especialidad médica que se ocupa del estudio y tratamiento de las enfermedades de la piel.')
INTO EspecialidadDoctores (especialidad_doctor, descripcion_especialidad) VALUES ('Ginecología', 'Rama de la medicina que se dedica al cuidado del sistema reproductor femenino.')
SELECT * FROM dual;
 
---- SELECT PARA VERIFICAR EL ESTADO DE INSERTS EN LA TABLA DE ESPECIALIDAD DOCTORES --------- 

SELECT * FROM EspecialidadDoctores;

------- TABLA DEPARTAMENTOS -------

CREATE TABLE Departamentos (
id_departamento INT PRIMARY KEY,
nombre_departamento VARCHAR2(50) NOT NULL
);

SELECT d.id_doctor, d.foto_doctor, d.correo_doctor, d.nombre_doctor, ed.especialidad_doctor AS Especialidad_Medica, um.nombre_unidad AS Unidad_Medica FROM Doctores d INNER JOIN EspecialidadDoctores ed ON d.id_especialidad = ed.id_especialidad INNER JOIN UnidadesMedicas um ON d.id_unidad = um.id_unidad


-------- SECUENCIA PARA LA TABLA DE DEPARTAMENTOS ---------

CREATE SEQUENCE sec_departamentos
START WITH 1
INCREMENT BY 1;

------ TRIGGER PARA LA CREACION DE IDS PARA LA TABLA DE DEPARTAMENTOS -------

CREATE TRIGGER TrigDepartamentos
BEFORE INSERT ON Departamentos
FOR EACH ROW
BEGIN
SELECT sec_departamentos.NEXTVAL INTO : NEW.id_departamento
FROM DUAL;
END;

-------- INSERTS PARA LA TABLA DE DEPARTAMENTOS ---------

INSERT ALL
INTO Departamentos (nombre_departamento) VALUES ('Ahuachapán')
INTO Departamentos (nombre_departamento) VALUES ('Santa Ana')
INTO Departamentos (nombre_departamento) VALUES ('Sonsonate')
INTO Departamentos (nombre_departamento) VALUES ('Chalatenango')
INTO Departamentos (nombre_departamento) VALUES ('La Libertad')
INTO Departamentos (nombre_departamento) VALUES ('San Salvador')
INTO Departamentos (nombre_departamento) VALUES ('Cuscatlán')
INTO Departamentos (nombre_departamento) VALUES ('La Paz')
INTO Departamentos (nombre_departamento) VALUES ('Cabañas')
INTO Departamentos (nombre_departamento) VALUES ('San Vicente')
INTO Departamentos (nombre_departamento) VALUES ('Usulután')
INTO Departamentos (nombre_departamento) VALUES ('San Miguel')
INTO Departamentos (nombre_departamento) VALUES ('Morazán')
INTO Departamentos (nombre_departamento) VALUES ('La Unión')
SELECT * FROM dual;

------ SELECT PARA VERIFICAR EL ESTADO DE INSERTS DE LA TABLA DE DEPARTAMENTOS ---------

SELECT * FROM Departamentos;

-------- TABLA DE UNIDADES MEDICAS --------

CREATE TABLE UnidadesMedicas(
id_unidad INT PRIMARY KEY,
nombre_unidad VARCHAR2(100) NOT NULL,
direccion VARCHAR2(200) NOT NULL,
id_departamento INT,
CONSTRAINT fk_departamentos
FOREIGN KEY (id_departamento) REFERENCES Departamentos(id_departamento)
);

------- SECUENCIA PARA LA TABLA DE UNIDADES MEDICAS --------

CREATE SEQUENCE sec_unidades_medicas
START WITH 1
INCREMENT BY 1;

------- TRIGGER PARA LA CREACION DE IDS PARA LA TABLA DE UNIDADES MEDICAS ---------

CREATE TRIGGER TrigUnidadesMedicas
BEFORE INSERT ON UnidadesMedicas
FOR EACH ROW
BEGIN
SELECT sec_unidades_medicas.NEXTVAL INTO : NEW.id_unidad
FROM DUAL;
END;

------- INSERTS PARA LA TABLA DE UNIDADES MEDICAS --------

INSERT ALL
INTO UnidadesMedicas (nombre_unidad, direccion, id_departamento) VALUES ('Hospital General', 'Alameda Roosevelt y 25a Av. Norte', 6)
INTO UnidadesMedicas (nombre_unidad, direccion, id_departamento) VALUES ('Policlínico Arce', 'Av. Independencia y 17 Calle Oriente', 6)
INTO UnidadesMedicas (nombre_unidad, direccion, id_departamento) VALUES ('Unidad Médica Atlacatl', 'Av. Atlacatl y 3a Calle Poniente', 6)
INTO UnidadesMedicas (nombre_unidad, direccion, id_departamento) VALUES ('Clínica Comunal Mejicanos', 'Calle a Mejicanos y 5a Av. Sur, Mejicanos', 6)
INTO UnidadesMedicas (nombre_unidad, direccion, id_departamento) VALUES ('Unidad Médica Soyapango', 'Boulevard del Ejército, Soyapango', 6)
INTO UnidadesMedicas (nombre_unidad, direccion, id_departamento) VALUES ('Hospital ISSS Santa Ana', '15 Calle Poniente y Av. Independencia', 2)
INTO UnidadesMedicas (nombre_unidad, direccion, id_departamento) VALUES ('Unidad Médica Chalchuapa', 'Avenida Principal y 3a Calle Oriente, Chalchuapa', 2)
INTO UnidadesMedicas (nombre_unidad, direccion, id_departamento) VALUES ('Policlínico Zacamil', 'Calle Zacamil y 12a Av. Norte', 6)
INTO UnidadesMedicas (nombre_unidad, direccion, id_departamento) VALUES ('Unidad Médica San Miguel', '4a Calle Poniente y 8a Av. Sur', 12)
INTO UnidadesMedicas (nombre_unidad, direccion, id_departamento) VALUES ('Clínica Comunal Ahuachapán', 'Boulevard Las Américas y 6a Av. Norte', 1)
INTO UnidadesMedicas (nombre_unidad, direccion, id_departamento) VALUES ('Unidad Médica Usulután', 'Calle Roosevelt y 3a Av. Sur', 11)
INTO UnidadesMedicas (nombre_unidad, direccion, id_departamento) VALUES ('Hospital ISSS San Vicente', 'Calle Independencia y 3a Av. Norte', 10)
INTO UnidadesMedicas (nombre_unidad, direccion, id_departamento) VALUES ('Unidad Médica La Libertad', 'Calle Principal y 2a Av. Sur', 5)
INTO UnidadesMedicas (nombre_unidad, direccion, id_departamento) VALUES ('Clínica Comunal Sonsonate', 'Av. Las Américas y 1a Calle Oriente', 3)
SELECT * FROM dual;

------ SELECT PARA VERIFICAR EL ESTADO DE INSERTS DE LA TABLA DE UNIDADES MEDICAS ------

SELECT * FROM Doctores;

---- TABLA DOCTORES ---------

CREATE TABLE Doctores(
id_doctor INT PRIMARY KEY,
correo_doctor VARCHAR2(100) NOT NULL,
contrasena_doctor VARCHAR2(255) UNIQUE NOT NULL,
nombre_doctor VARCHAR2(100) NOT NULL,
foto_doctor VARCHAR2(255) NOT NULL,
id_especialidad INT,
id_unidad INT,
CONSTRAINT fk_especialidad_doctores
FOREIGN KEY (id_especialidad) REFERENCES EspecialidadDoctores(id_especialidad),
CONSTRAINT fk_unidad_medica
FOREIGN KEY (id_unidad) REFERENCES UnidadesMedicas(id_unidad)
);

------- SECUENCIA PARA LA TABLA DE DOCTORES --------

CREATE SEQUENCE sec_doctores
START WITH 1
INCREMENT BY 1;

------- TRIGGER PARA IDS DE LA TABLA DE DOCTORES --------

CREATE TRIGGER TrigDoctores
BEFORE INSERT ON Doctores
FOR EACH ROW
BEGIN 
SELECT sec_doctores.NEXTVAL INTO : NEW.id_doctor
FROM DUAL;
END;

------- INSERTS PARA LA TABLA DE DOCTORES ----------

INSERT ALL
INTO Doctores (correo_doctor, contrasena_doctor, nombre_doctor, foto_doctor, id_especialidad, id_unidad) VALUES ('carlos_garcía@isss.gob.sv', 'carlosgarcia#001', 'Carlos García', 'https://familydoctor.org/wp-content/uploads/2018/02/41808433_l.jpg', 1, 2)
INTO Doctores (correo_doctor, contrasena_doctor, nombre_doctor, foto_doctor, id_especialidad, id_unidad) VALUES ('carlos_hernandez@isss.gob.sv', 'carloshernandez#002', 'Carlos Hernández', 'https://t3.ftcdn.net/jpg/02/60/04/08/360_F_260040863_fYxB1SnrzgJ9AOkcT0hoe7IEFtsPiHAD.jpg', 2, 3)
INTO Doctores (correo_doctor, contrasena_doctor, nombre_doctor, foto_doctor, id_especialidad, id_unidad) VALUES ('daniel_park@isss.gob.sv', 'danielpark#003', 'Daniel Park', 'https://online-learning-college.com/wp-content/uploads/2023/01/Qualifications-to-Become-a-Doctor--scaled.jpg', 3, 4)
INTO Doctores (correo_doctor, contrasena_doctor, nombre_doctor, foto_doctor, id_especialidad, id_unidad) VALUES ('mark_rojas@isss.gob.sv', 'markrojas#004', 'Mark Rojas', 'https://cdn.punchng.com/wp-content/uploads/2023/09/20144750/Umoh-Michael-e1695217670244.jpeg', 4, 5)
INTO Doctores (correo_doctor, contrasena_doctor, nombre_doctor, foto_doctor, id_especialidad, id_unidad) VALUES ('briana_lewis@isss.gob.sv', 'brianalewis#005', 'Briana Lewis', 'https://static.vecteezy.com/system/resources/thumbnails/028/287/555/small_2x/an-indian-young-female-doctor-isolated-on-green-ai-generated-photo.jpg', 5, 6)
SELECT * FROM dual;

------- SELECT PARA VERIFICAR EL ESTADO DE INSERTS EN LA TABLA DOCTORES ----------

SELECT * FROM Doctores;

------ TABLA DE EXPEDIENTES MEDICOS --------

CREATE TABLE ExpedientesMedicos(
id_expediente INT PRIMARY KEY,
diagnostico VARCHAR2(300) NOT NULL,
tratamiento VARCHAR2(300) NOT NULL,
notas VARCHAR2(300) NOT NULL,
id_usuario INT,
CONSTRAINT fk_usuario_expdiente
FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

------ SECUENCIA PARA LA TABLA DE EXPEDIENTES MEDICOS -------

CREATE SEQUENCE sec_expedientes_medicos
START WITH 1
INCREMENT BY 1;

------- TRIGGER PARA LA CREACIÓN DE IDS EN LA TABLA DE EXPEDIENTES MEDICOS -------

CREATE TRIGGER TrigExpedientesMedicos
BEFORE INSERT ON ExpedientesMedicos
FOR EACH ROW
BEGIN
SELECT sec_expedientes_medicos.NEXTVAL INTO : NEW.id_expediente
FROM DUAL;
END;

------ INSERTS PARA LA TABLA DE EXPEDIENTES MEDICOS ------

INSERT ALL
INTO ExpedientesMedicos (diagnostico, tratamiento, notas, id_usuario) VALUES ('Hipertensión arterial', 'Medicamentos antihipertensivos y dieta baja en sodio', 'Paciente presenta presión arterial elevada desde hace 6 meses', 1)
INTO ExpedientesMedicos (diagnostico, tratamiento, notas, id_usuario) VALUES ('Diabetes tipo 2', 'Insulina y control de glucosa diaria', 'Paciente diagnosticado con diabetes tipo 2 hace 2 años, sin complicaciones mayores', 2)
INTO ExpedientesMedicos (diagnostico, tratamiento, notas, id_usuario) VALUES ('Asma', 'Inhalador y evitar alergenos conocidos', 'Paciente presenta ataques de asma intermitentes, controlado con medicación', 3)
INTO ExpedientesMedicos (diagnostico, tratamiento, notas, id_usuario) VALUES ('Gastritis crónica', 'Inhibidores de la bomba de protones y dieta balanceada', 'Paciente presenta molestias gástricas recurrentes, se recomienda seguimiento con gastroenterólogo', 4)
INTO ExpedientesMedicos (diagnostico, tratamiento, notas, id_usuario) VALUES ('Anemia ferropénica', 'Suplementos de hierro y dieta rica en hierro', 'Paciente presenta síntomas de fatiga y palidez, se recomienda reevaluación en 3 meses', 5)
SELECT * FROM dual;

----- SELECT PARA VERIFICAR EL ESTADO DE INSERTS EN LA TABLA DE EXPEDIENTES MEDICOS -------

SELECT * FROM ExpedientesMedicos;

----- TABLA DE SOLICITUD CITAS -------

CREATE TABLE SolicitudCitas(
id_solicitud INT PRIMARY KEY,
nombre_solicitante VARCHAR2(50) NOT NULL,
motivo_cita VARCHAR2(300) NOT NULL,
fecha_solicitud DATE NOT NULL,
id_usuario INT,
CONSTRAINT fk_usuario_solicitud
FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

----- SECUENCIA PARA LA TABLA SOLICITUD CITAS --------

CREATE SEQUENCE sec_solicitud_citas
START WITH 1
INCREMENT BY 1;

------ TRIGGER PARA LA CREACION DE IDS PARA LA TABLA DE SOLICITUD CITAS ----------

CREATE TRIGGER TrigSolicitudCitas
BEFORE INSERT ON SolicitudCitas
FOR EACH ROW
BEGIN
SELECT sec_solicitud_citas.NEXTVAL INTO : NEW.id_solicitud
FROM DUAL;
END;

-------- INSERTS PARA LA TABLA DE SOLICITUD CITAS -------

INSERT ALL
INTO SolicitudCitas (nombre_solicitante, motivo_cita, fecha_solicitud, id_usuario) VALUES ('Diana Padilla', 'Chequeo anual general', '04/06/2024', 1)
INTO SolicitudCitas (nombre_solicitante, motivo_cita, fecha_solicitud, id_usuario) VALUES ('Diego Arriaza', 'Dolor de espalda persistente', '05/06/2024', 2)
INTO SolicitudCitas (nombre_solicitante, motivo_cita, fecha_solicitud, id_usuario) VALUES ('Ricardo García', 'Consulta dermatológica por erupción cutánea', '06/06/2024', 3)
INTO SolicitudCitas (nombre_solicitante, motivo_cita, fecha_solicitud, id_usuario) VALUES ('Kevin Alvarado', 'Evaluación de niveles de glucosa', '07/06/2024', 4)
INTO SolicitudCitas (nombre_solicitante, motivo_cita, fecha_solicitud, id_usuario) VALUES ('Oscar Rauda', 'Dolor de cabeza frecuente', '08/06/2024', 5)
INTO SolicitudCitas (nombre_solicitante, motivo_cita, fecha_solicitud, id_usuario) VALUES ('Luis Escalante', 'Revisión dental', '09/06/2024', 6)
SELECT * FROM dual;

------ SELECT PARA VERIFICAR EL ESTADO DE INSERTS EN LA TABLA DE SOLICITUD CITAS ------

SELECT * FROM SolicitudCitas;

------- TABLA DE CITAS MEDICAS ----------

CREATE TABLE CitasMedicas(
id_cita INT PRIMARY KEY,
fecha_cita DATE NOT NULL,
hora_cita VARCHAR2(10) NOT NULL,
id_usuario INT,
id_doctor INT,
CONSTRAINT fk_usuario_cita
FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario),
CONSTRAINT fk_doctor_cita
FOREIGN KEY (id_doctor) REFERENCES Doctores(id_doctor)
);

------- SECUENCIA PARA LA TABLA DE CITAS MEDICAS ------

CREATE SEQUENCE sec_citas_medicas
START WITH 1
INCREMENT BY 1;

------- TRIGGER PARA LA CREACION DE IDS EN LA TABLA DE CITAS MEDICAS ------

CREATE TRIGGER TrigCitasMedicas
BEFORE INSERT ON CitasMedicas
FOR EACH ROW
BEGIN
SELECT sec_citas_medicas.NEXTVAL INTO : NEW.id_cita
FROM DUAL;
END;

------ INSERTS PARA LA TABLA DE CITAS MEDICAS ------

INSERT ALL
INTO CitasMedicas (fecha_cita, hora_cita, id_usuario, id_doctor) VALUES ('10/06/2024', '09:00 A.M.', 1, 2)
INTO CitasMedicas (fecha_cita, hora_cita, id_usuario, id_doctor) VALUES ('11/06/2024', '10:30 A.M.', 2, 3)
INTO CitasMedicas (fecha_cita, hora_cita, id_usuario, id_doctor) VALUES ('12/06/2024', '11:00 A.M.', 3, 4)
INTO CitasMedicas (fecha_cita, hora_cita, id_usuario, id_doctor) VALUES ('13/06/2024', '14:00 P.M.', 4, 5)
INTO CitasMedicas (fecha_cita, hora_cita, id_usuario, id_doctor) VALUES ('14/06/2024', '15:30 P.M.', 5, 1)
SELECT * FROM dual;

----- SELECT PARA VERIFICAR EL ESTADO DE INSERTS EN LA TABLA DE CITAS MEDICAS -------

SELECT * FROM CitasMedicas;

----- INNER JOIN PARA MOSTRAR DATOS DE LAS TABLAS DE CITAS MEDICAS, USUARIOS Y DOCTORES --------

SELECT 
    cm.id_cita,
    cm.fecha_cita,
    cm.hora_cita,
    u.correo_electronico AS solicitante,
    d.nombre_doctor AS doctor
FROM 
    CitasMedicas cm
INNER JOIN 
    Usuarios u ON cm.id_usuario = u.id_usuario
INNER JOIN 
    Doctores d ON cm.id_doctor = d.id_doctor;
    
----- TABLA DE NOTICIAS MEDICAS ---------

CREATE TABLE NoticiasMedicas(
id_noticia INT PRIMARY KEY,
imagen_noticia VARCHAR2(255) NOT NULL,
titulo_noticia VARCHAR2(50) NOT NULL,
descripcion_noticia VARCHAR2(300) NOT NULL,
fecha_noticia DATE NOT NULL
);

------ SECUENCIA PARA LA TABLA DE NOTICIAS -------

CREATE SEQUENCE sec_noticias
START WITH 1
INCREMENT BY 1;

------ TRIGGER PARA LA CREACION DE NOTICIAS EN LA TABLA DE NOTICIAS MEDICAS --------

CREATE TRIGGER TrigNoticias
BEFORE INSERT ON NoticiasMedicas
FOR EACH ROW
BEGIN
SELECT sec_noticias.NEXTVAL INTO : NEW.id_noticia
FROM DUAL;
END;

-------- INSERTS PARA LA TABLA DE NOTICIAS MEDICAS ---------

INSERT ALL
INTO NoticiasMedicas (imagen_noticia, titulo_noticia, descripcion_noticia, fecha_noticia) VALUES ('https://assets.isu.pub/document-structure/221202135314-8bfec2c0d7608fd0d553c24ebfe0b5f5/v1/29936b33b893d1f39c0072c4379fd64e.jpeg', 'Avances en Cardiología', 'Nuevas investigaciones revelan tratamientos prometedores para enfermedades cardíacas.', '07/07/2024')
INTO NoticiasMedicas (imagen_noticia, titulo_noticia, descripcion_noticia, fecha_noticia) VALUES ('https://diariofarma.com/wp-content/uploads/2023/10/Depositphotos_16169157_XL-600x400.jpg', 'Vacunas Innovadoras', 'Se desarrollan vacunas que podrían erradicar enfermedades raras en los próximos años.', '08/07/2024')
INTO NoticiasMedicas (imagen_noticia, titulo_noticia, descripcion_noticia, fecha_noticia) VALUES ('https://www.aceprensa.com/wp-content/uploads/2019/09/La-terapia-g%C3%A9nica-echa-a-andar.jpg', 'Terapias Génicas', 'Las terapias génicas abren nuevas puertas en el tratamiento de enfermedades genéticas.', '09/07/2024')
INTO NoticiasMedicas (imagen_noticia, titulo_noticia, descripcion_noticia, fecha_noticia) VALUES ('https://micarreralaboralenit.wordpress.com/wp-content/uploads/2020/01/salud-mental-trabajador.jpg', 'Salud Mental en el Trabajo', 'Estudios demuestran la importancia de la salud mental en entornos laborales para la productividad.', '10/07/2024')
INTO NoticiasMedicas (imagen_noticia, titulo_noticia, descripcion_noticia, fecha_noticia) VALUES ('https://www.nestle-centroamerica.com/sites/g/files/pydnoa521/files/2024-05/Untitled%20design%20%2833%29.png', 'Nutrición y Bienestar', 'Investigaciones recientes destacan la relación entre una dieta equilibrada y el bienestar general.', '11/07/2024')
SELECT * FROM dual;

------ SELECT PARA VERIFICAR EL ESTADO DE INSERTS DE LA TABLA DE NOTICIAS MEDICAS -------

SELECT * FROM NoticiasMedicas;

------ TRIGGER PARA LA ASIGNACION DEL ID_ROL = 2 PARA EL REGISTRO DE PACIENTES PARA LA APLICACIÓN MOVIL EN EL CASO DE QUE SEA NULO ------

CREATE OR REPLACE TRIGGER set_default_rol
BEFORE INSERT ON Usuarios
FOR EACH ROW
BEGIN
IF : NEW.id_rol IS NULL THEN
   : NEW.id_rol := 2;
   END IF;
END;

------- TRIGGER PARA LA ASIGNACION DEL ID_USUARIOS EN CASO DE QUE SEA NULO -------

CREATE OR REPLACE TRIGGER set_default_usuario
BEFORE INSERT ON SolicitudCitas
FOR EACH ROW
BEGIN
IF : NEW.id_usuario IS NULL THEN
   : NEW.id_usuario := 1;
   END IF;
END;

------ TRIGGER PARA LA ASIGNACIÓN DE UNA IMAGEN DEFAULT EN EL CASO DE QUE EL CAMPO SEA NULO -------

CREATE OR REPLACE TRIGGER set_default_image
BEFORE INSERT ON Usuarios
FOR EACH ROW 
BEGIN
IF: NEW.foto_usuario IS NULL THEN
  : NEW.foto_usuario := 'https://i.pinimg.com/236x/2a/2e/7f/2a2e7f0f60b750dfb36c15c268d0118d.jpg';
  END IF;
  END;
  
------ INNER JOIN PARA MOSTRAR DATOS DE LAS TABLAS DE DOCTORES, ESPECIALIDAD DOCTORES Y UNIDADES MEDICAS --------

SELECT 
    d.id_doctor,
    d.foto_doctor,
    d.correo_doctor,
    d.nombre_doctor,
    ed.especialidad_doctor AS Especialidad_Medica,
    um.nombre_unidad AS Unidad_Medica
FROM 
    Doctores d
INNER JOIN 
    EspecialidadDoctores ed ON d.id_especialidad = ed.id_especialidad
INNER JOIN 
    UnidadesMedicas um ON d.id_unidad = um.id_unidad;