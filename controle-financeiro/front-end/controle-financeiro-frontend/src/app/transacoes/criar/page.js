
'use client';

import { useState, useEffect } from 'react';
import { Container, Form, Button, Alert } from 'react-bootstrap';
import { createTransaction, getCategories } from '../../../lib/api';
import { useRouter } from 'next/navigation';

export default function CriarTransacao() {
  const [formData, setFormData] = useState({
    descricao: '',
    valor: '',
    data: '2025-07-01',
    tipo: 'SAIDA',
    usuarioId: '1',
    categoriaId: '1',
  });
  const [categorias, setCategorias] = useState([]);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const router = useRouter();

  useEffect(() => {
    async function fetchCategorias() {
      try {
        const data = await getCategories();
        setCategorias(data);
      } catch (err) {
        setError('Erro ao carregar categorias.');
      }
    }
    fetchCategorias();
  }, []);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await createTransaction({
        ...formData,
        valor: Number(formData.valor),
        usuarioId: Number(formData.usuarioId),
        categoriaId: Number(formData.categoriaId),
      });
      setSuccess('Transação criada com sucesso!');
      setError(null);
      setFormData({ ...formData, descricao: '', valor: '' });
      setTimeout(() => router.push('/transacoes'), 2000);
    } catch (err) {
      setError(err.response?.data?.message || 'Erro ao criar transação.');
      setSuccess(null);
    }
  };

  return (
    <Container className="mt-4">
      <h2>Criar Transação</h2>
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>Descrição</Form.Label>
          <Form.Control
            type="text"
            name="descricao"
            value={formData.descricao}
            onChange={handleChange}
            placeholder="Digite a descrição"
            required
          />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>Valor</Form.Label>
          <Form.Control
            type="number"
            name="valor"
            value={formData.valor}
            onChange={handleChange}
            placeholder="Digite o valor"
            required
            min="0.01"
            step="0.01"
          />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>Data</Form.Label>
          <Form.Control
            type="date"
            name="data"
            value={formData.data}
            onChange={handleChange}
            max="2025-07-01"
            required
          />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>Tipo</Form.Label>
          <Form.Select name="tipo" value={formData.tipo} onChange={handleChange}>
            <option value="ENTRADA">Entrada</option>
            <option value="SAIDA">Saída</option>
          </Form.Select>
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>ID do Usuário</Form.Label>
          <Form.Control
            type="number"
            name="usuarioId"
            value={formData.usuarioId}
            onChange={handleChange}
            placeholder="Digite o ID do usuário"
            required
          />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>Categoria</Form.Label>
          <Form.Select
            name="categoriaId"
            value={formData.categoriaId}
            onChange={handleChange}
            required
          >
            <option value="">Selecione uma categoria</option>
            {categorias.map(cat => (
              <option key={cat.id} value={cat.id}>{cat.nome}</option>
            ))}
          </Form.Select>
        </Form.Group>
        <Button variant="primary" type="submit">
          Salvar
        </Button>
        <Button variant="secondary" className="ms-2" onClick={() => router.push('/transacoes')}>
          Cancelar
        </Button>
      </Form>

      {error && <Alert variant="danger" className="mt-3">{error}</Alert>}
      {success && <Alert variant="success" className="mt-3">{success}</Alert>}
    </Container>
  );
}
