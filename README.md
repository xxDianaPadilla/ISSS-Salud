CREATE TABLE Roles(
id_rol INT PRIMARY KEY,
nombre_rol VARCHAR2(50) NOT NULL,
descripcion_rol VARCHAR2(250) NOT NULL
);

CREATE SEQUENCE sec_rol
START WITH 1
INCREMENT BY 1;

CREATE TRIGGER TrigRol
BEFORE INSERT ON Roles
FOR EACH ROW
BEGIN
SELECT sec_rol.NEXTVAL INTO: NEW.id_rol
FROM DUAL;
END;

CREATE TABLE Usuarios(
id_usuario INT PRIMARY KEY,
dui VARCHAR2(10) UNIQUE NOT NULL,
tipo_sangre VARCHAR2(5) NOT NULL,
telefono VARCHAR2(9)UNIQUE NOT NULL,
foto_usuario VARCHAR2(255) NULL,
correo_electronico VARCHAR2(100) NOT NULL,
contrasena VARCHAR2(255) UNIQUE NOT NULL,
id_rol INT,
CONSTRAINT fk_rol
FOREIGN KEY (id_rol) REFERENCES Roles(id_rol)
);

CREATE SEQUENCE sec_usuarios
START WITH 1
INCREMENT BY 1;

CREATE TRIGGER TrigUsuarios
BEFORE INSERT ON Usuarios
FOR EACH ROW
BEGIN
SELECT sec_usuarios.NEXTVAL INTO : NEW.id_usuario
FROM DUAL;
END;

CREATE TABLE EspecialidadDoctores(
id_especialidad INT PRIMARY KEY,
especialidad_doctor VARCHAR2(100) NOT NULL,
descripcion_especialidad VARCHAR2(200) NOT NULL
);

CREATE SEQUENCE sec_especialidad_doctores 
START WITH 1
INCREMENT BY 1;

CREATE TRIGGER TrigEspecialidadDoctores
BEFORE INSERT ON EspecialidadDoctores
FOR EACH ROW
BEGIN
SELECT sec_especialidad_doctores.NEXTVAL INTO : NEW.id_especialidad
FROM DUAL;
END;

CREATE TABLE Departamentos (
id_departamento INT PRIMARY KEY,
nombre_departamento VARCHAR2(50) NOT NULL
);

CREATE SEQUENCE sec_departamentos
START WITH 1
INCREMENT BY 1;

CREATE TRIGGER TrigDepartamentos
BEFORE INSERT ON Departamentos
FOR EACH ROW
BEGIN
SELECT sec_departamentos.NEXTVAL INTO : NEW.id_departamento
FROM DUAL;
END;

CREATE TABLE UnidadesMedicas(
id_unidad INT PRIMARY KEY,
nombre_unidad VARCHAR2(100) NOT NULL,
direccion VARCHAR2(200) NOT NULL,
id_departamento INT,
CONSTRAINT fk_departamentos
FOREIGN KEY (id_departamento) REFERENCES Departamentos(id_departamento)
);

CREATE SEQUENCE sec_unidades_medicas
START WITH 1
INCREMENT BY 1;

CREATE TRIGGER TrigUnidadesMedicas
BEFORE INSERT ON UnidadesMedicas
FOR EACH ROW
BEGIN
SELECT sec_unidades_medicas.NEXTVAL INTO : NEW.id_unidad
FROM DUAL;
END;

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

CREATE SEQUENCE sec_doctores
START WITH 1
INCREMENT BY 1;

CREATE TRIGGER TrigDoctores
BEFORE INSERT ON Doctores
FOR EACH ROW
BEGIN 
SELECT sec_doctores.NEXTVAL INTO : NEW.id_doctor
FROM DUAL;
END;

CREATE TABLE ExpedientesMedicos(
id_expediente INT PRIMARY KEY,
diagnostico VARCHAR2(300) NOT NULL,
tratamiento VARCHAR2(300) NOT NULL,
notas VARCHAR2(300) NOT NULL,
id_usuario INT,
CONSTRAINT fk_usuario_expdiente
FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

CREATE SEQUENCE sec_expedientes_medicos
START WITH 1
INCREMENT BY 1;

CREATE TRIGGER TrigExpedientesMedicos
BEFORE INSERT ON ExpedientesMedicos
FOR EACH ROW
BEGIN
SELECT sec_expedientes_medicos.NEXTVAL INTO : NEW.id_expediente
FROM DUAL;
END;

CREATE TABLE SolicitudCitas(
id_solicitud INT PRIMARY KEY,
nombre_solicitante VARCHAR2(50) NOT NULL,
motivo_cita VARCHAR2(300) NOT NULL,
fecha_solicitud DATE NOT NULL,
id_usuario INT,
CONSTRAINT fk_usuario_solicitud
FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

CREATE SEQUENCE sec_solicitud_citas
START WITH 1
INCREMENT BY 1;

CREATE TRIGGER TrigSolicitudCitas
BEFORE INSERT ON SolicitudCitas
FOR EACH ROW
BEGIN
SELECT sec_solicitud_citas.NEXTVAL INTO : NEW.id_solicitud
FROM DUAL;
END;

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

CREATE SEQUENCE sec_citas_medicas
START WITH 1
INCREMENT BY 1;

CREATE TRIGGER TrigCitasMedicas
BEFORE INSERT ON CitasMedicas
FOR EACH ROW
BEGIN
SELECT sec_citas_medicas.NEXTVAL INTO : NEW.id_cita
FROM DUAL;
END;

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

CREATE TABLE NoticiasMedicas(
id_noticia INT PRIMARY KEY,
imagen_noticia VARCHAR2(255) NOT NULL,
titulo_noticia VARCHAR2(50) NOT NULL,
descripcion_noticia VARCHAR2(300) NOT NULL,
fecha_noticia DATE NOT NULL
);

CREATE SEQUENCE sec_noticias
START WITH 1
INCREMENT BY 1;

CREATE TRIGGER TrigNoticias
BEFORE INSERT ON NoticiasMedicas
FOR EACH ROW
BEGIN
SELECT sec_noticias.NEXTVAL INTO : NEW.id_noticia
FROM DUAL;
END;

CREATE OR REPLACE TRIGGER set_default_rol
BEFORE INSERT ON Usuarios
FOR EACH ROW
BEGIN
IF : NEW.id_rol IS NULL THEN
   : NEW.id_rol := 2;
   END IF;
END;

CREATE OR REPLACE TRIGGER set_default_usuario
BEFORE INSERT ON SolicitudCitas
FOR EACH ROW
BEGIN
IF : NEW.id_usuario IS NULL THEN
   : NEW.id_usuario := 1;
   END IF;
END;

CREATE OR REPLACE TRIGGER set_default_image
BEFORE INSERT ON Usuarios
FOR EACH ROW 
BEGIN
IF: NEW.foto_usuario IS NULL THEN
  : NEW.foto_usuario := 'https://i.pinimg.com/236x/2a/2e/7f/2a2e7f0f60b750dfb36c15c268d0118d.jpg';
  END IF;
  END;
  
SELECT * FROM Doctores;
SELECT * FROM EspecialidadDoctores;
SELECT * FROM UnidadesMedicas;

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
