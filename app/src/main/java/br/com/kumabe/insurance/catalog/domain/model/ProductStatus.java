package br.com.kumabe.insurance.catalog.domain.model;

public enum ProductStatus {
    DRAFT {
        @Override
        public boolean canTransitionTo(ProductStatus next) {
            return next == ACTIVE || next == ARCHIVED;
        }
    },
    ACTIVE {
        @Override
        public boolean canTransitionTo(ProductStatus next) {
            return next == DEPRECATED || next == SUSPENDED || next == ARCHIVED;
        }
    },
    SUSPENDED {
        @Override
        public boolean canTransitionTo(ProductStatus next) {
            return next == ACTIVE || next == ARCHIVED;
        }
    },
    DEPRECATED {
        @Override
        public boolean canTransitionTo(ProductStatus next) {
            return next == ARCHIVED;
        }
    },
    ARCHIVED {
        @Override
        public boolean canTransitionTo(ProductStatus next) {
            return false; // Estado terminal
        }
    };

    /**
     * Valida se a transição para o próximo estado é permitida pela máquina de estados.
     */
    public abstract boolean canTransitionTo(ProductStatus next);

    public boolean isSellable() {
        return this == ACTIVE;
    }
}