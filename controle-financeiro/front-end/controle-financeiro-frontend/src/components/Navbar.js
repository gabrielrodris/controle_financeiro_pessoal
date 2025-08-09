'use client';

import Link from 'next/link';
import { Navbar as BSNavbar, Nav, Container } from 'react-bootstrap';

export default function Navbar() {
  return (
    <BSNavbar bg="light" expand="lg" className="mb-4">
      <Container>
        <BSNavbar.Brand as={Link} href="/">Controle Financeiro</BSNavbar.Brand>
        <BSNavbar.Toggle aria-controls="basic-navbar-nav" />
        <BSNavbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link as={Link} href="/">Dashboard</Nav.Link>
            <Nav.Link as={Link} href="/transacoes">Transações</Nav.Link>
            <Nav.Link as={Link} href="/transacoes/criar">Criar Transação</Nav.Link>
            <Nav.Link as={Link} href="/metas">Metas Financeiras</Nav.Link>
            <Nav.Link as={Link} href="/categorias">Categorias</Nav.Link>
          </Nav>
        </BSNavbar.Collapse>
      </Container>
    </BSNavbar>
  );
}