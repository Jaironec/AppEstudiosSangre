import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';

function App() {
  const [bloodStudies, setBloodStudies] = useState([]);
  const [nombreCompleto, setNombreCompleto] = useState('');
  const [porcentajeAzucar, setPorcentajeAzucar] = useState(0);
  const [porcentajeGrasa, setPorcentajeGrasa] = useState(0);
  const [porcentajeOxigeno, setPorcentajeOxigeno] = useState(0);
  const [isLoading, setIsLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedBloodStudy, setSelectedBloodStudy] = useState(null);

  useEffect(() => {
    fetchBloodStudies();
  }, []);

  const fetchBloodStudies = async () => {
    try {
      setIsLoading(true);
      const response = await axios.get('http://localhost:8080/api/blood-studies');
      setBloodStudies(response.data);
      setIsLoading(false);
    } catch (error) {
      console.error(error);
      setIsLoading(false);
    }
  };

  const createBloodStudy = async () => {
    try {
      setIsLoading(true);
      const response = await axios.post('http://localhost:8080/api/blood-studies', {
        nombreCompleto,
        porcentajeAzucar,
        porcentajeGrasa,
        porcentajeOxigeno
      });
      setBloodStudies([...bloodStudies, response.data]);
      setIsLoading(false);
      resetForm();
    } catch (error) {
      console.error(error);
      setIsLoading(false);
    }
  };

  const updateBloodStudy = async () => {
    try {
      setIsLoading(true);
      const response = await axios.put(`http://localhost:8080/api/blood-studies/${selectedBloodStudy.id}`, {
        nombreCompleto,
        porcentajeAzucar,
        porcentajeGrasa,
        porcentajeOxigeno
      });
      const updatedBloodStudies = bloodStudies.map((study) => {
        if (study.id === selectedBloodStudy.id) {
          return response.data;
        }
        return study;
      });
      setBloodStudies(updatedBloodStudies);
      setIsLoading(false);
      resetForm();
    } catch (error) {
      console.error(error);
      setIsLoading(false);
    }
  };

  const deleteBloodStudy = async (id) => {
    try {
      setIsLoading(true);
      await axios.delete(`http://localhost:8080/api/blood-studies/${id}`);
      const updatedBloodStudies = bloodStudies.filter((study) => study.id !== id);
      setBloodStudies(updatedBloodStudies);
      setIsLoading(false);
    } catch (error) {
      console.error(error);
      setIsLoading(false);
    }
  };

  const resetForm = () => {
    setNombreCompleto('');
    setPorcentajeAzucar(0);
    setPorcentajeGrasa(0);
    setPorcentajeOxigeno(0);
    setSelectedBloodStudy(null);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (selectedBloodStudy) {
      updateBloodStudy();
    } else {
      createBloodStudy();
    }
  };

  const handleSearch = (e) => {
    setSearchTerm(e.target.value);
  };

  const filteredBloodStudies = bloodStudies.filter((study) => {
    return study.nombreCompleto.toLowerCase().includes(searchTerm.toLowerCase());
  });

  const handleEdit = (study) => {
    setNombreCompleto(study.nombreCompleto);
    setPorcentajeAzucar(study.porcentajeAzucar);
    setPorcentajeGrasa(study.porcentajeGrasa);
    setPorcentajeOxigeno(study.porcentajeOxigeno);
    setSelectedBloodStudy(study);
  };

  return (
    <div className="App">
      <h1>Estudios de Sangre</h1>
      <div className="form-container">
        <form onSubmit={handleSubmit}>
          <label htmlFor="nombreCompleto">Nombre Completo:</label>
          <input
            type="text"
            id="nombreCompleto"
            value={nombreCompleto}
            onChange={(e) => setNombreCompleto(e.target.value)}
            required
          />

          <label htmlFor="porcentajeAzucar">Porcentaje de Azúcar:</label>
          <input
            type="number"
            id="porcentajeAzucar"
            value={porcentajeAzucar}
            onChange={(e) => setPorcentajeAzucar(parseFloat(e.target.value))}
            required
          />

          <label htmlFor="porcentajeGrasa">Porcentaje de Grasa:</label>
          <input
            type="number"
            id="porcentajeGrasa"
            value={porcentajeGrasa}
            onChange={(e) => setPorcentajeGrasa(parseFloat(e.target.value))}
            required
          />

          <label htmlFor="porcentajeOxigeno">Porcentaje de Oxígeno:</label>
          <input
            type="number"
            id="porcentajeOxigeno"
            value={porcentajeOxigeno}
            onChange={(e) => setPorcentajeOxigeno(parseFloat(e.target.value))}
            required
          />

          <div className="buttons-container">
            {selectedBloodStudy ? (
              <div>
                <button type="button" onClick={resetForm}>Cancelar</button>
                <button type="submit">Actualizar</button>
              </div>
            ) : (
              <button type="submit">Crear</button>
            )}
          </div>
        </form>
      </div>

      <div className="search-container">
        <label htmlFor="searchTerm">Buscar por nombre completo:</label>
        <input
          type="text"
          id="searchTerm"
          value={searchTerm}
          onChange={handleSearch}
          placeholder="Ingrese el nombre completo"
        />
      </div>

      <h2>Lista de Estudios de Sangre</h2>
      {isLoading ? (
        <p>Cargando...</p>
      ) : filteredBloodStudies.length === 0 ? (
        <p>No hay estudios de sangre registrados.</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Nombre Completo</th>
              <th>Porcentaje de Azúcar</th>
              <th>Porcentaje de Grasa</th>
              <th>Porcentaje de Oxígeno</th>
              <th>Nivel de Riesgo</th>
              <th>Fecha de Creación</th>
              <th>Fecha de Actualización</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {filteredBloodStudies.map((study) => (
              <tr key={study.id}>
                <td>{study.nombreCompleto}</td>
                <td>{study.porcentajeAzucar}</td>
                <td>{study.porcentajeGrasa}</td>
                <td>{study.porcentajeOxigeno}</td>
                <td>{study.nivelRiesgo}</td>
                <td>{study.fechaCreacion ? new Date(study.fechaCreacion).toLocaleString() : ''}</td>
                <td>{study.fechaActualizacion ? new Date(study.fechaActualizacion).toLocaleString() : ''}</td>
                <td>
                  <button onClick={() => handleEdit(study)}>Editar</button>
                  <button onClick={() => deleteBloodStudy(study.id)}>Eliminar</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default App;
