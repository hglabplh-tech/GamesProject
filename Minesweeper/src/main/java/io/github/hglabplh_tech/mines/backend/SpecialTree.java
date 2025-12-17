/*
Copyright (c) 2025 Harald Glab-Plhak

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package io.github.hglabplh_tech.mines.backend;

import javax.swing.tree.TreeNode;
import java.util.Objects;
import java.util.Optional;

public class SpecialTree {

    private TreeElement root = new TreeElement()
            .newBuilder()
            .build();


    public SpecialTree() {

    }

    public TreeElement initRoot(ButtonPoint rootPoint) {
        TreeElement newRoot = root.copyBuilder()
                .thisPointOpt(rootPoint)
                .build();
        this.root = newRoot;
        return this.root;
    }

    public TreeElement newTreeElement(TreeElement parent, ButtonPoint value) {
        TreeElement element = new TreeElement(); // can be done in builders
        return element.copyBuilder()
                .parentOpt(parent)
                .thisPointOpt(value).build();
    }

    public TreeElement insertLeft(TreeElement parent, TreeElement left) {
        return parent.copyBuilder()
                .leftOpt(left)
                .build();


    }

    public TreeElement insertRight(TreeElement parent, TreeElement right) {
        return parent.copyBuilder()
                .rightOpt(right)
                .build();

    }

    public TreeElement insertLeftRight(TreeElement parent, TreeElement left,
                                       TreeElement right) {
        return parent.copyBuilder()
                .leftOpt(left)
                .rightOpt(right)
                .build();

    }

    public void setSuccess(TreeElement element, Boolean result) {
        element.setIsSuccess(result);
    }

    // have to think about that it is not complete
    public TreeElement traverse(TreeElement element) {
        TreeElement result = element;
        if (element.isSuccess()) {
            if (element.getLeft() != null) {
                result = traverse(element.getLeft());
            }
            if (element.getRight() != null) {
                result = traverse(element.getRight());
            }
            return result;
        } else {
            return element.getParent();
        }
    }


    public static class TreeElement {
        private ButtonPoint thisPoint;
        private TreeElement parent;
        private TreeElement left;
        private TreeElement right;
        private Boolean success = false;


        public TreeElement() {
        }

        public ButtonPoint getThisPoint() {
            return thisPoint;
        }

        public TreeElement getParent() {
            return this.parent;
        }

        public TreeElement getLeft() {
            return left;
        }

        public TreeElement getRight() {
            return right;
        }

        public Boolean isSuccess() {
            return this.success;
        }

        public void setIsSuccess(Boolean value) {
            this.success = value;
        }

        public Builder newBuilder() {
            return new Builder();
        }

        public Builder copyBuilder() {
            return new Builder(this);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            TreeElement that = (TreeElement) o;
            return Objects.equals(getThisPoint(), that.getThisPoint())
                    && Objects.equals(getParent(), that.getParent())
                    && Objects.equals(getLeft(), that.getLeft())
                    && Objects.equals(getRight(), that.getRight());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getThisPoint(), getParent(), getLeft(), getRight());
        }

        public static class Builder {
            private Optional<ButtonPoint> thisPointOpt = Optional.empty();
            private Optional<TreeElement> parentOpt = Optional.empty();
            private Optional<TreeElement> leftOpt = Optional.empty();
            private Optional<TreeElement> rightOpt = Optional.empty();

            public Builder() {
            }

            public Builder(TreeElement toCopy) {
                this.thisPointOpt = Optional.of(toCopy.getThisPoint());
                this.parentOpt = Optional.of(toCopy.getParent());
                this.leftOpt = Optional.of(toCopy.getLeft());
                this.rightOpt = Optional.of(toCopy.getRight());
            }

            public Builder thisPointOpt(ButtonPoint point) {
                this.thisPointOpt = Optional.of(point);
                return this;
            }

            public Builder parentOpt(TreeElement parent) {
                this.parentOpt = Optional.of(parent);
                return this;
            }


            public Builder leftOpt(TreeElement left) {
                this.leftOpt = Optional.of(left);
                return this;
            }

            public Builder rightOpt(TreeElement right) {
                this.rightOpt = Optional.of(right);
                return this;
            }

            public TreeElement build() {
                TreeElement element = new TreeElement();
                element.thisPoint = this.thisPointOpt.orElse(null);
                element.parent = this.parentOpt.orElse(null);
                element.left = this.leftOpt.orElse(null);
                element.right = this.rightOpt.orElse(null);
                return element;
            }

        }
    }
}
