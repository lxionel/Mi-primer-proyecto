package com.peripollos.view;

import com.peripollos.dao.*;
import com.peripollos.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MainView extends JFrame {
    private Usuario usuarioActual;
    private ProductoDAO productoDAO;
    private PedidoDAO pedidoDAO;
    private CajaDAO cajaDAO; 
    private Caja cajaActiva; 

    private JLabel lblUsuario;
    private JButton btnNuevoPedido;
    private JButton btnCerrarSesion;
    private JButton btnReportes;
    private JButton btnCaja;

    private Pedido pedidoActual;
    private DefaultTableModel modeloTabla;
    private JTable tablaDetalles;
    private JLabel lblTotal;
    private JLabel lblEstadoCaja; 

    private JPanel panelProductos;
    private JPanel panelIzquierdo; 
    private JPanel panelDerecho; 
    private JSplitPane splitPane; 

    public MainView(Usuario usuario) {
        this.usuarioActual = usuario;
        this.productoDAO = new ProductoDAO();
        this.pedidoDAO = new PedidoDAO();
        this.cajaDAO = new CajaDAO(); 

        inicializarComponentes();
        cargarProductos();
        nuevoPedido();
        actualizarEstadoCaja();
    }

    private void inicializarComponentes() {
        setTitle("Peripollos - Sistema de Ventas");
        setSize(1300, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(248, 249, 250));

        JPanel panelHeader = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(43, 146, 209), getWidth(), 0, new Color(17, 116, 145));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelHeader.setPreferredSize(new Dimension(1300, 90));

        JLabel lblTitulo = new JLabel("POLLERÍA PERIPOLLOS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));

        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelUsuario.setOpaque(false);

        if (usuarioActual.esAdministrador()) {
            btnReportes = new JButton("📊 Reportes");
            btnReportes.setFont(new Font("Arial", Font.BOLD, 13));
            btnReportes.setBackground(new Color(0, 123, 255));
            btnReportes.setForeground(Color.WHITE);
            btnReportes.setFocusPainted(false);
            btnReportes.setBorderPainted(false);
            btnReportes.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnReportes.setPreferredSize(new Dimension(120, 38));
            btnReportes.addActionListener(e -> mostrarReportes());

            btnCaja = new JButton("💰 Caja");
            btnCaja.setFont(new Font("Arial", Font.BOLD, 13));
            btnCaja.setBackground(new Color(255, 193, 7));
            btnCaja.setForeground(Color.BLACK);
            btnCaja.setFocusPainted(false);
            btnCaja.setBorderPainted(false);
            btnCaja.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnCaja.setPreferredSize(new Dimension(100, 38));
            btnCaja.addActionListener(e -> gestionarCaja());

            panelUsuario.add(btnReportes);
            panelUsuario.add(btnCaja);
        }

        lblEstadoCaja = new JLabel("Cargando...");
        lblEstadoCaja.setFont(new Font("Arial", Font.BOLD, 14));
        lblEstadoCaja.setForeground(Color.WHITE);
        panelUsuario.add(lblEstadoCaja);
        panelUsuario.add(Box.createHorizontalStrut(15));
   

        JPanel infoUsuario = new JPanel();
        infoUsuario.setLayout(new BoxLayout(infoUsuario, BoxLayout.Y_AXIS));
        infoUsuario.setOpaque(false);

        JLabel lblNombre = new JLabel(usuarioActual.nombre());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 16));
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblRol = new JLabel(usuarioActual.rol());
        lblRol.setFont(new Font("Arial", Font.PLAIN, 12));
        lblRol.setForeground(new Color(255, 255, 255, 200));
        lblRol.setAlignmentX(Component.RIGHT_ALIGNMENT);

        infoUsuario.add(lblNombre);
        infoUsuario.add(lblRol);

        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Arial", Font.BOLD, 13));
        btnCerrarSesion.setBackground(new Color(108, 117, 125));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setPreferredSize(new Dimension(130, 38));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        panelUsuario.add(infoUsuario);
        panelUsuario.add(btnCerrarSesion);
        panelUsuario.add(Box.createHorizontalStrut(15));

        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelHeader.add(panelUsuario, BorderLayout.EAST);

      
        JPanel panelCentral = new JPanel(new BorderLayout(15, 15));
        panelCentral.setBackground(new Color(248, 249, 250));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

  
        panelIzquierdo = new JPanel(new BorderLayout(0, 15)); // <-- MODIFICADO
        panelIzquierdo.setBackground(Color.WHITE);
        panelIzquierdo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblMenu = new JLabel("MENÚ DE PRODUCTOS");
        lblMenu.setFont(new Font("Arial", Font.BOLD, 20));
        lblMenu.setForeground(new Color(33, 37, 41));

        panelProductos = new JPanel();
        panelProductos.setLayout(new GridLayout(0, 2, 15, 15));
        panelProductos.setBackground(Color.WHITE);
        JScrollPane scrollProductos = new JScrollPane(panelProductos);
        scrollProductos.setBorder(null);
        scrollProductos.getVerticalScrollBar().setUnitIncrement(16);

        panelIzquierdo.add(lblMenu, BorderLayout.NORTH);
        panelIzquierdo.add(scrollProductos, BorderLayout.CENTER);

       
        panelDerecho = crearPanelPedido();

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzquierdo, panelDerecho); 
        splitPane.setDividerLocation(620);
        splitPane.setDividerSize(8);
        splitPane.setBorder(null);
        panelCentral.add(splitPane);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        add(panelPrincipal);
    }


    private JPanel crearPanelPedido() {
        JPanel panelDerecho = new JPanel(new BorderLayout(0, 15));
        panelDerecho.setBackground(Color.WHITE);
        panelDerecho.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JPanel panelSuperiorDerecho = new JPanel(new BorderLayout());
        panelSuperiorDerecho.setBackground(Color.WHITE);

        JLabel lblPedido = new JLabel("PEDIDO EN RESTAURANTE");
        lblPedido.setFont(new Font("Arial", Font.BOLD, 20));
        lblPedido.setForeground(new Color(220, 53, 69));

        btnNuevoPedido = new JButton("+ Nuevo Pedido");
        btnNuevoPedido.setFont(new Font("Arial", Font.BOLD, 13));
        btnNuevoPedido.setBackground(new Color(0, 123, 255));
        btnNuevoPedido.setForeground(Color.WHITE);
        btnNuevoPedido.setFocusPainted(false);
        btnNuevoPedido.setBorderPainted(false);
        btnNuevoPedido.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNuevoPedido.setPreferredSize(new Dimension(140, 38));
        btnNuevoPedido.addActionListener(e -> nuevoPedido());

        panelSuperiorDerecho.add(lblPedido, BorderLayout.WEST);
        panelSuperiorDerecho.add(btnNuevoPedido, BorderLayout.EAST);

   
        String[] columnas = {"Producto", "Precio Unit.", "Cantidad", "Subtotal"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaDetalles = new JTable(modeloTabla);
        tablaDetalles.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaDetalles.setRowHeight(40);
        tablaDetalles.setGridColor(new Color(233, 236, 239));
        tablaDetalles.setSelectionBackground(new Color(232, 244, 255));
        tablaDetalles.setSelectionForeground(Color.BLACK);
        tablaDetalles.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tablaDetalles.getTableHeader().setBackground(new Color(248, 249, 250));
        tablaDetalles.getTableHeader().setForeground(new Color(33, 37, 41));
        tablaDetalles.getTableHeader().setPreferredSize(new Dimension(0, 45));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaDetalles.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tablaDetalles.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        tablaDetalles.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

        tablaDetalles.getColumnModel().getColumn(0).setPreferredWidth(200);
        tablaDetalles.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablaDetalles.getColumnModel().getColumn(2).setPreferredWidth(80);
        tablaDetalles.getColumnModel().getColumn(3).setPreferredWidth(100);

        JScrollPane scrollTabla = new JScrollPane(tablaDetalles);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230), 1));

        // Panel inferior
        JPanel panelInferior = new JPanel(new BorderLayout(0, 15));
        panelInferior.setBackground(Color.WHITE);

        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        panelTotal.setBackground(new Color(248, 249, 250));
        panelTotal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTextoTotal = new JLabel("TOTAL:");
        lblTextoTotal.setFont(new Font("Arial", Font.BOLD, 24));
        lblTextoTotal.setForeground(new Color(33, 37, 41));

        lblTotal = new JLabel("S/ 0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 36));
        lblTotal.setForeground(new Color(43, 146, 209));

        panelTotal.add(lblTextoTotal);
        panelTotal.add(lblTotal);

        JButton btnGuardar = new JButton("GUARDAR PEDIDO");
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 16));
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setPreferredSize(new Dimension(250, 55));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> guardarPedido());

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.setBackground(Color.WHITE);
        panelBoton.add(btnGuardar);

        panelInferior.add(panelTotal, BorderLayout.NORTH);
        panelInferior.add(panelBoton, BorderLayout.CENTER);

        panelDerecho.add(panelSuperiorDerecho, BorderLayout.NORTH);
        panelDerecho.add(scrollTabla, BorderLayout.CENTER);
        panelDerecho.add(panelInferior, BorderLayout.SOUTH);

        return panelDerecho;
    }

    private void cargarProductos() {
        try {
            List<Producto> productos = productoDAO.listarTodos();
            panelProductos.removeAll();

            for (Producto producto : productos) {
                JButton btnProducto = new JButton("<html><center>" +
                        producto.nombre() + "<br><b>S/ " +
                        String.format("%.2f", producto.precio()) + "</b></center></html>");
                btnProducto.setFont(new Font("Arial", Font.BOLD, 13));
                btnProducto.setPreferredSize(new Dimension(150, 60));
                btnProducto.setFocusPainted(false);
                btnProducto.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnProducto.setBorderPainted(true);
                btnProducto.setOpaque(true);

                if ("POLLO".equals(producto.categoria())) {
                    btnProducto.setBackground(Color.WHITE);
                    btnProducto.setForeground(Color.BLACK);
                    btnProducto.setBorder(BorderFactory.createLineBorder(new Color(255, 193, 7), 3));
                } else if ("GUARNICION".equals(producto.categoria())) {
                    btnProducto.setBackground(Color.WHITE);
                    btnProducto.setForeground(Color.BLACK);
                    btnProducto.setBorder(BorderFactory.createLineBorder(new Color(40, 167, 69), 3));
                } else {
                    btnProducto.setBackground(Color.WHITE);
                    btnProducto.setForeground(Color.BLACK);
                    btnProducto.setBorder(BorderFactory.createLineBorder(new Color(0, 123, 255), 3));
                }

                btnProducto.addActionListener(e -> agregarProducto(producto));
                panelProductos.add(btnProducto);
            }

          
            panelProductos.revalidate();
            panelProductos.repaint();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar productos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void nuevoPedido() {
        pedidoActual = new Pedido("RESTAURANTE", usuarioActual);
        modeloTabla.setRowCount(0);
        actualizarTotal();
    }

    private void agregarProducto(Producto producto) {
        if (pedidoActual == null) {
            nuevoPedido();
        }

        String cantidad = JOptionPane.showInputDialog(this,
                "Cantidad de " + producto.nombre() + ":", "1");

        if (cantidad != null && !cantidad.trim().isEmpty()) {
            try {
                int cant = Integer.parseInt(cantidad);
                if (cant > 0) {
                    DetallePedido detalle = new DetallePedido(producto, cant);
                    pedidoActual.agregarDetalle(detalle);

                    modeloTabla.addRow(new Object[]{
                            producto.nombre(),
                            "S/ " + String.format("%.2f", producto.precio()),
                            cant,
                            "S/ " + String.format("%.2f", detalle.subtotal())
                    });

                    actualizarTotal();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Cantidad inválida",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarTotal() {
        if (pedidoActual != null) {
            lblTotal.setText("S/ " + String.format("%.2f", pedidoActual.total()));
        } else {
            lblTotal.setText("S/ 0.00");
        }
    }

    private void guardarPedido() {
        if (pedidoActual == null || pedidoActual.detalles().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay productos en el pedido",
                    "Pedido vacío",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean guardado = pedidoDAO.insertar(pedidoActual);

            if (guardado) {
                JTextArea textoBoleta = new JTextArea(pedidoActual.generarDocumento());
                textoBoleta.setFont(new Font("Monospaced", Font.PLAIN, 12));
                textoBoleta.setEditable(false);

                JOptionPane.showMessageDialog(this,
                        new JScrollPane(textoBoleta),
                        "Pedido Guardado Exitosamente",
                        JOptionPane.PLAIN_MESSAGE);

                nuevoPedido();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al guardar el pedido",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error de base de datos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarReportes() {
        try {
            double totalVentas = pedidoDAO.obtenerTotalVentasDelDia();
            List<Pedido> pedidosHoy = pedidoDAO.listarPedidosDelDia();

            StringBuilder reporte = new StringBuilder();
            reporte.append(" ════════════════════════════════════════════\n");
            reporte.append("       REPORTE DE VENTAS DEL DÍA     \n");
            reporte.append(" ════════════════════════════════════════════\n\n");
            reporte.append("  Total de pedidos: ").append(pedidosHoy.size()).append("\n");
            reporte.append("  Total vendido: S/ ").append(String.format("%.2f", totalVentas)).append("\n\n");
            reporte.append(" ────────────────────────────────────────────\n");
            reporte.append("         DETALLE DE PEDIDOS:\n");
            reporte.append(" ────────────────────────────────────────────\n\n");

            if (pedidosHoy.isEmpty()) {
                reporte.append("  No hay pedidos registrados hoy.\n");
            } else {
                for (Pedido pedido : pedidosHoy) {
                  
                    reporte.append("  Pedido #").append(pedido.id());
                    reporte.append(" - S/ ").append(String.format("%.2f", pedido.total()));
                    reporte.append(" - ").append(pedido.usuario().nombre()).append("\n");

                    
                    try {
                        String sqlDetalles = "SELECT pr.nombre, dp.cantidad, dp.subtotal " +
                                "FROM detalle_pedido dp " +
                                "INNER JOIN productos pr ON dp.id_producto = pr.id " +
                                "WHERE dp.id_pedido = ?";

                        java.sql.Connection conn = com.peripollos.db.ConexionDB.obtenerConexion();
                        java.sql.PreparedStatement stmt = conn.prepareStatement(sqlDetalles);

                        stmt.setInt(1, pedido.id());
                        java.sql.ResultSet rs = stmt.executeQuery();

                        while (rs.next()) {
                            reporte.append("  • ").append(rs.getInt("cantidad")).append("x ");
                            reporte.append(rs.getString("nombre"));
                            reporte.append(" - S/ ").append(String.format("%.2f", rs.getDouble("subtotal")));
                            reporte.append("\n");
                        }

                        rs.close();
                        stmt.close();
                        conn.close();
                    } catch (Exception e) {
                        reporte.append("  (Error al cargar detalles: ").append(e.getMessage()).append(")\n");
                    }

                    reporte.append("\n");
                }
            }

            JTextArea areaReporte = new JTextArea(reporte.toString());
            areaReporte.setFont(new Font("Monospaced", Font.PLAIN, 12));
            areaReporte.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(areaReporte);
            scrollPane.setPreferredSize(new Dimension(275, 400));

            JOptionPane.showMessageDialog(this,
                    scrollPane,
                    "📊 Reportes - Solo Administrador",
                    JOptionPane.PLAIN_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al generar reporte: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void gestionarCaja() {
        String[] opciones = {"Abrir Caja", "Cerrar Caja", "Cancelar"};
        int seleccion = JOptionPane.showOptionDialog(this,
                "Seleccione una opción:",
                "💰 Gestión de Caja - Solo Administrador",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        try {
            
            Caja cajaAbierta = cajaDAO.obtenerCajaAbierta();

            if (seleccion == 0) { 
                if (cajaAbierta != null) {
                    JOptionPane.showMessageDialog(this,
                            "Ya existe una caja abierta por " + cajaAbierta.usuario().nombre() + ".\n" +
                                    "Debe cerrarla antes de abrir una nueva.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String montoStr = JOptionPane.showInputDialog(this, "Monto inicial de caja:", "100.00");
                if (montoStr != null && !montoStr.trim().isEmpty()) {
                    try {
                        double montoInicial = Double.parseDouble(montoStr);
                        Caja nuevaCaja = new Caja(montoInicial, usuarioActual);

                        boolean exito = cajaDAO.abrirCaja(nuevaCaja);

                        if (exito) {
                            JOptionPane.showMessageDialog(this,
                                    "Caja abierta exitosamente con S/ " + String.format("%.2f", montoInicial),
                                    "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            throw new SQLException("No se pudo guardar la apertura de caja.");
                        }

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Monto inválido", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            } else if (seleccion == 1) { 
                if (cajaAbierta == null) {
                    JOptionPane.showMessageDialog(this,
                            "No hay ninguna caja abierta para cerrar.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

               
                double montoInicial = cajaAbierta.montoInicial();
                double totalVentas = pedidoDAO.obtenerTotalVentasDelDia();
                double totalEsperado = montoInicial + totalVentas;

                String montoFinalStr = JOptionPane.showInputDialog(this,
                        "Ventas del día: S/ " + String.format("%.2f", totalVentas) + "\n" +
                                "Monto Inicial: S/ " + String.format("%.2f", montoInicial) + "\n" +
                                "TOTAL ESPERADO: S/ " + String.format("%.2f", totalEsperado) + "\n\n" +
                                "Por favor, ingrese el monto final contado en caja:",
                        "Cierre de Caja",
                        JOptionPane.QUESTION_MESSAGE);

                if (montoFinalStr != null && !montoFinalStr.trim().isEmpty()) {
                    try {
                        double montoFinalContado = Double.parseDouble(montoFinalStr);

                      
                        cajaAbierta.cerrarCaja(montoFinalContado);

                        boolean exito = cajaDAO.cerrarCaja(cajaAbierta);

                        if (exito) {
                            
                            String reporte = cajaAbierta.generarDocumento();
                            reporte = reporte.replace("Monto Final:   S/ " + String.format("%.2f", montoFinalContado),
                                    "Monto Contado: S/ " + String.format("%.2f", montoFinalContado) + "\n" +
                                            "Monto Esperado: S/ " + String.format("%.2f", totalEsperado) + "\n" +
                                            String.format("Diferencia:    S/ %.2f", (montoFinalContado - totalEsperado)));

                            JTextArea areaReporte = new JTextArea(reporte);
                            areaReporte.setFont(new Font("Monospaced", Font.PLAIN, 12));
                            areaReporte.setEditable(false);

                            JOptionPane.showMessageDialog(this,
                                    new JScrollPane(areaReporte),
                                    "Caja Cerrada Exitosamente",
                                    JOptionPane.PLAIN_MESSAGE);
                        } else {
                            throw new SQLException("No se pudo guardar el cierre de caja.");
                        }

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Monto inválido", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error de base de datos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        actualizarEstadoCaja(); 
    }

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea cerrar sesión?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginView().setVisible(true);
        }
    }


    private void setComponentesEnabled(Component component, boolean enabled) {
        component.setEnabled(enabled);
        if (component instanceof Container) {
            for (Component c : ((Container) component).getComponents()) {
                
                if (c == btnReportes || c == btnCaja || c == btnCerrarSesion) {
                    continue;
                }
                setComponentesEnabled(c, enabled);
            }
        }
  
        if (btnCaja != null) {
            btnCaja.setEnabled(true);
        }
    }

    private void actualizarEstadoCaja() {
        try {
            cajaActiva = cajaDAO.obtenerCajaAbierta();

            if (cajaActiva != null) {
      
                lblEstadoCaja.setText(String.format("CAJA ABIERTA (Fondo: S/ %.2f)", cajaActiva.montoInicial()));
                lblEstadoCaja.setForeground(new Color(144, 238, 144)); 

                setComponentesEnabled(panelIzquierdo, true);
                setComponentesEnabled(panelDerecho, true);

            } else {
                lblEstadoCaja.setText("CAJA CERRADA");
                lblEstadoCaja.setForeground(new Color(240, 128, 128));

                setComponentesEnabled(panelIzquierdo, false);
                setComponentesEnabled(panelDerecho, false);
            }
        } catch (SQLException e) {
            lblEstadoCaja.setText("ERROR DE CAJA");
            lblEstadoCaja.setForeground(Color.ORANGE);
            JOptionPane.showMessageDialog(this, "Error al verificar estado de caja: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
