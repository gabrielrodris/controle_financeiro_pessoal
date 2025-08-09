'use client';

import { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Alert } from 'react-bootstrap';
import { getTransactionsByUserAndCategory, getFinancialGoals } from '../lib/api';

export default function Dashboard() {
  const [transacoes, setTransacoes] = useState([]);
  const [metas, setMetas] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function fetchData() {
      try {
        const transacoesData = await getTransactionsByUserAndCategory(1, 1);
        const metasData = await getFinancialGoals(1);
        setTransacoes(transacoesData);
        setMetas(metasData);
        setError(null);
      } catch (err) {
        setError('Erro ao carregar dados. Verifique a conexão com o servidor.');
      }
    }
    fetchData();
  }, []);

  return (
    <Container className="mt-4">
      <h1>Dashboard</h1>
      {error && <Alert variant="danger">{error}</Alert>}
      <Row>
        <Col md={4}>
          <Card className="mb-4">
            <Card.Body>
              <Card.Title>Saldo Total</Card.Title>
              <Card.Text>
                Entradas: R$ {transacoes.reduce((sum, t) => t.tipo === 'ENTRADA' ? sum + t.valor : sum, 0).toFixed(2)} <br />
                Saídas: R$ {transacoes.reduce((sum, t) => t.tipo === 'SAIDA' ? sum + t.valor : sum, 0).toFixed(2)} <br />
                Saldo: R$ {(transacoes.reduce((sum, t) => t.tipo === 'ENTRADA' ? sum + t.valor : sum - t.valor, 0)).toFixed(2)}
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>
        <Col md={8}>
          <Card className="mb-4">
            <Card.Body>
              <Card.Title>Metas Financeiras</Card.Title>
              {metas.map(meta => (
                <div key={meta.id} className="mb-2">
                  <strong>{meta.nome}</strong>: R$ {meta.valorObjetivo.toFixed(2)} até {meta.dataLimite}
                </div>
              ))}
            </Card.Body>
          </Card>
        </Col>
      </Row>
      <Row>
        <Col>
          <Card>
            <Card.Body>
              <Card.Title>Transações Recentes</Card.Title>
              <table className="table table-striped">
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
                  {transacoes.slice(0, 5).map(t => (
                    <tr key={t.id}>
                      <td>{t.id}</td>
                      <td>{t.descricao}</td>
                      <td>R$ {t.valor.toFixed(2)}</td>
                      <td>{t.data}</td>
                      <td>{t.tipo}</td>
                      <td>{t.categoriaNome}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
}