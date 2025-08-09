'use client';

import { useState, useEffect } from 'react';
import { Container, Form, Button, Table, Alert, Modal, Badge, Spinner } from 'react-bootstrap';
import { getCategories, createCategory, updateCategory, deleteCategory, getCategoriesByType } from '../../lib/api';

export default function GerenciamentoCategorias() {
  const [categorias, setCategorias] = useState([]);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [modalType, setModalType] = useState('create');
  const [currentCategory, setCurrentCategory] = useState({
    id: null,
    nome: '',
    tipo: 'DESPESA'
  });
  const [loading, setLoading] = useState(false);
  const [filterType, setFilterType] = useState('TODAS');

  useEffect(() => {
    fetchCategorias();
  }, [filterType]);

  const fetchCategorias = async () => {
    setLoading(true);
    try {
      let data;
      if (filterType === 'TODAS') {
        data = await getCategories();
      } else {
        data = await getCategoriesByType(filterType);
      }
      setCategorias(data);
      setError(null);
    } catch (err) {
      setError('Erro ao carregar categorias.');
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setCurrentCategory({
      ...currentCategory,
      [name]: value
    });
  };

  const handleSubmit = async () => {
    try {
      if (modalType === 'create') {
        await createCategory(currentCategory);
        setSuccess('Categoria criada com sucesso!');
      } else {
        await updateCategory(currentCategory.id, currentCategory);
        setSuccess('Categoria atualizada com sucesso!');
      }
      fetchCategorias();
      handleCloseModal();
    } catch (err) {
      setError(err.message || 'Erro ao processar a requisição.');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir esta categoria?')) {
      try {
        await deleteCategory(id);
        setSuccess('Categoria excluída com sucesso!');
        fetchCategorias();
      } catch (err) {
        setError('Erro ao excluir categoria. Verifique se não há transações vinculadas.');
      }
    }
  };

  const handleOpenCreateModal = () => {
    setCurrentCategory({
      id: null,
      nome: '',
      tipo: 'DESPESA'
    });
    setModalType('create');
    setShowModal(true);
  };

  const handleOpenEditModal = (categoria) => {
    setCurrentCategory({
      id: categoria.id,
      nome: categoria.nome,
      tipo: categoria.tipo
    });
    setModalType('edit');
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };

  const handleFilterChange = (e) => {
    setFilterType(e.target.value);
  };

  return (
    <Container className="mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Gerenciamento de Categorias</h2>
        <Button variant="primary" onClick={handleOpenCreateModal}>
          Nova Categoria
        </Button>
      </div>

      <div className="mb-3">
        <Form.Select value={filterType} onChange={handleFilterChange}>
          <option value="TODAS">Todas as categorias</option>
          <option value="DESPESA">Apenas despesas</option>
          <option value="RECEITA">Apenas receitas</option>
        </Form.Select>
      </div>

      {error && <Alert variant="danger" onClose={() => setError(null)} dismissible>{error}</Alert>}
      {success && <Alert variant="success" onClose={() => setSuccess(null)} dismissible>{success}</Alert>}

      {loading ? (
        <div className="text-center">
          <Spinner animation="border" role="status">
            <span className="visually-hidden">Carregando...</span>
          </Spinner>
        </div>
      ) : (
        <Table striped bordered hover responsive>
          <thead>
            <tr>
              <th>ID</th>
              <th>Nome</th>
              <th>Tipo</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {categorias.length > 0 ? (
              categorias.map(cat => (
                <tr key={cat.id}>
                  <td>{cat.id}</td>
                  <td>{cat.nome}</td>
                  <td>
                    <Badge bg={cat.tipo === 'RECEITA' ? 'success' : 'danger'}>
                      {cat.tipo}
                    </Badge>
                  </td>
                  <td>
                    <Button 
                      variant="outline-primary" 
                      size="sm" 
                      onClick={() => handleOpenEditModal(cat)}
                      className="me-2"
                    >
                      Editar
                    </Button>
                    <Button 
                      variant="outline-danger" 
                      size="sm" 
                      onClick={() => handleDelete(cat.id)}
                    >
                      Excluir
                    </Button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={4} className="text-center">
                  Nenhuma categoria encontrada.
                </td>
              </tr>
            )}
          </tbody>
        </Table>
      )}

      <Modal show={showModal} onHide={handleCloseModal}>
        <Modal.Header closeButton>
          <Modal.Title>
            {modalType === 'create' ? 'Nova Categoria' : 'Editar Categoria'}
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Nome da Categoria*</Form.Label>
              <Form.Control
                type="text"
                name="nome"
                value={currentCategory.nome}
                onChange={handleInputChange}
                placeholder="Ex: Alimentação, Transporte, Salário"
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Tipo*</Form.Label>
              <Form.Select
                name="tipo"
                value={currentCategory.tipo}
                onChange={handleInputChange}
                required
              >
                <option value="DESPESA">Despesa</option>
                <option value="RECEITA">Receita</option>
              </Form.Select>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseModal}>
            Cancelar
          </Button>
          <Button variant="primary" onClick={handleSubmit}>
            {modalType === 'create' ? 'Criar' : 'Salvar Alterações'}
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
}