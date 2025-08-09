import Navbar from '../components/Navbar';
import './globals.css';

export const metadata = {
  title: 'Controle Financeiro',
  description: 'Gerencie suas finanças pessoais',
  manifest: '/manifest.json',
};

export default function RootLayout({ children }) {
  return (
    <html lang="pt-BR">
      <body>
        <Navbar />
        {children}
      </body>
    </html>
  );
}