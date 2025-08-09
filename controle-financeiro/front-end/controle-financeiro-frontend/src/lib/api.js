import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
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
  const response = await api.get(`/metas-financeiras/usuario/${usuarioId}`);
  return response.data;
};

export const createFinancialGoal = async (data) => {
  const response = await api.post('/metas-financeiras', data);
  return response.data;
};