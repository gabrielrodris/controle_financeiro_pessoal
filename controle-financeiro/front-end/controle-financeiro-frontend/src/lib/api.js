import axios from 'axios';

const api = axios.create({
  baseURL: 'http://192.168.1.161:8081', // IP do PC e porta 8080
  headers: {
    'Content-Type': 'application/json',
  },
});

export const getTransactionsByUserAndCategory = async (usuarioId, categoriaId) => {
  const response = await api.get(`/transacoes/usuario/${usuarioId}/categoria/${categoriaId}`);
  return response.data;
};

export const createTransaction = async (data) => {
  const response = await api.post('/transacoes', data);
  return response.data;
};

export const getCategories = async () => {
  const response = await api.get('/categorias');
  return response.data;
};

export const getFinancialGoals = async (usuarioId) => {
  const response = await api.get(`/metaFinanceiras/usuario/${usuarioId}`);
  return response.data;
};

export const createFinancialGoal = async (data) => {
  const response = await api.post('/metaFinanceiras', data);
  return response.data;
};