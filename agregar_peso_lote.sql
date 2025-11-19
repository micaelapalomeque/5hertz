-- Agregar columna peso_lote_kg a la tabla lote_proceso
ALTER TABLE lote_proceso ADD COLUMN peso_lote_kg DECIMAL(10,3) DEFAULT 0.0;
