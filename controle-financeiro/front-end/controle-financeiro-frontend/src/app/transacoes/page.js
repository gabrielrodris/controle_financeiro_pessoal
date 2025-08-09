
'use client';

import { useState, useEffect } from 'react';
import { Container, Form, Button, Table, Alert } from 'react-bootstrap';
import { getTransactionsByUserAndCategory, getCategories } from '../../lib/api';

export default function Transacoes() {
  const [usuarioId, setUsuarioId] = useState('1');
  const [categoriaId, setCategoriaId] = useState('1');
  const [categorias, setCategorias] = useState([]);
  const [transacoes, setTransacoes] = useState([]);
  const [error, setError] = useState(null);

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

  const fetchTransacoes = async () => {
    try {
      const data = await getTransactionsByUserAndCategory(Number(usuarioId), Number(categoriaId));
      setTransacoes(data);
      setError(null);
    } catch (err) {
      setError('Erro ao buscar transações. Verifique os IDs.');
      setTransacoes([]);
    }
  };

  useEffect(() => {
    if (usuarioId && categoriaId) {
      fetchTransacoes();
    }
  }, [usuarioId, categoriaId]);

  const handleSubmit = (e) => {
    e.preventDefault();
    fetchTransacoes();
  };

  return (
    <Container className="mt-4">
      <h2>Lista de Transações</h2>
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>ID do Usuário</Form.Label>
          <Form.Control
            type="number"
            value={usuarioId}
            onChange={(e) => setUsuarioId(e.target.value)}
            placeholder="Digite o ID do usuário"
            required
          />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>Categoria</Form.Label>
          <Form.Select
            value={categoriaId}
            onChange={(e) => setCategoriaId(e.target.value)}
            required
          >
            <option value="">Selecione uma categoria</option>
            {categorias.map(cat => (
              <option key={cat.id} value={cat.id}>{cat.nome}</option>
            ))}
          </Form.Select>
        </Form.Group>
        <Button variant="primary" type="submit">
          Filtrar
        </Button>
      </Form>

      {error && <Alert variant="danger" className="mt-3">{error}</Alert>}

      <Table striped bordered hover className="mt-3">
        <thead>
          <tr>
            <th>ID</th>
            <th>Descrição</th>
            <th>Valor</th>
            <th>Data</th>
            <th>Tipo</th>
            <th>Categoria</th>
          </tr>
        </thead>
        <tbody>
          {transacoes.length > 0 ? (
            transacoes.map(t => (
              <tr key={t.id}>
                <td>{t.id}</td>
                <td>{t.descricao}</td>
                <td>R$ {t.valor.toFixed(2)}</td>
                <td>{t.data}</td>
                <td>{t.tipo}</td>
                <td>{t.categoriaNome}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={6}>Nenhuma transação encontrada.</td>
            </tr>
          )}
        </tbody>
      </Table>
    </Container>
  );
}
