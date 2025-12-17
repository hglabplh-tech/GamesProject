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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class DecisionTree {

    private TreeElement root = new TreeElement()
            .newBuilder()
            .build();


    public DecisionTree() {

    }

    public TreeElement initRoot(ButtonPoint rootPoint) {
        TreeElement newRoot = root.changeBuilder()
                .thisPointOpt(rootPoint)
                .build();
        this.root = newRoot;
        return this.root;
    }

    public TreeElement newTreeElement(TreeElement parent, TreeElementType type, ButtonPoint value) {
        return this.root.copyBuilder()
                .parentOpt(parent)
                .elementTypeOpt(type)
                .thisPointOpt(value).build();
    }


    public TreeElement insertLeft(TreeElement parent, TreeElement left) {
        return parent.changeBuilder()
                .parentOpt(parent)
                .leftOpt(left)
                .build();
    }

    public TreeElement insertRight(TreeElement parent, TreeElement right) {
        return parent.changeBuilder()
                .parentOpt(parent)
                .rightOpt(right)
                .build();

    }

    public TreeElement insertSibling(TreeElement parent, TreeElement element, TreeElement sibling) {
        return element.changeBuilder()
                .parentOpt(parent)
                .siblingOpt(sibling)
                .build();

    }

    public TreeElement insertLeftRight(TreeElement parent, TreeElement left,
                                       TreeElement right) {
        return parent.changeBuilder()
                .parentOpt(parent)
                .leftOpt(left)
                .rightOpt(right)
                .build();

    }

    public TreeElement insertLeftRightSibling(TreeElement parent, TreeElement left,
                                       TreeElement right, TreeElement sibling) {
        return parent.changeBuilder()
                .parentOpt(parent)
                .leftOpt(left)
                .rightOpt(right)
                .siblingOpt(sibling)
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

    public TreeElement getRoot() {
        return root;
    }

    public static class TreeElement {
        private ButtonPoint thisPoint;
        private TreeElementType elementType;
        private TreeElement parent;
        private TreeElement left;
        private TreeElement right;
        private TreeElement sibling;
        private Boolean success = false;




        public TreeElement() {
        }

        public ButtonPoint getThisPoint() {
            return this.thisPoint;
        }

        public TreeElement getParent() {
            return this.parent;
        }

        public TreeElement getLeft() {
            return this.left;
        }

        public TreeElement getRight() {
            return this.right;
        }

        public TreeElement getSibling() {
            return this.sibling;
        }

        public Boolean isSuccess() {
            return this.success;
        }

        public void setIsSuccess(Boolean value) {
            this.success = value;
        }

        public TreeElementType getElementType () {
            return this.elementType;
        }

        public <T> TreeElement addSuccessor (TreeElement element,
                                         Predicate<? super T> predicate,
                                         T value) {
            return element
                    .changeBuilder()
                    .setSuccessor(predicate, value)
                    .build();
        }

        public <T> List<TreeElement> loopAndAddSuccessor (TreeElement element,
                                                          Predicate<? super T> predicate,
                                                          T value) {
            List<TreeElement> allElements = new ArrayList<>();
            if (element.getLeft() != null) {
                allElements.add(element.getLeft());
            }
            if (element.getRight() != null) {
                allElements.add(element.getRight());
            }
            if (element.getSibling() != null) {
                allElements.add(element.getSibling());
            }
            List<TreeElement> result = new ArrayList<>();

            for (TreeElement item: allElements) {
                TreeElement changed = item.addSuccessor(item, predicate, value);
                if (changed.isSuccess()) {
                    result.add(changed);
                }
            }
            return result;
        }



        public Builder newBuilder() {
            return new Builder();
        }

        public Builder copyBuilder() {
            return new Builder(this, true);
        }

        public Builder changeBuilder() {
            return new Builder(this, false);
        }


        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            TreeElement that = (TreeElement) o;
            return Objects.equals(getThisPoint(), that.getThisPoint())
                    && Objects.equals(getElementType(), that.getElementType())
                    && Objects.equals(getParent(), that.getParent())
                    && Objects.equals(getLeft(), that.getLeft())
                    && Objects.equals(getRight(), that.getRight())
                    && Objects.equals(getSibling(), that.getSibling())
                    && Objects.equals(this.isSuccess(), that.isSuccess());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getThisPoint(), getParent(), getLeft(), getRight(), getSibling(), success);
        }

        public static class Builder {
            private Optional<ButtonPoint> thisPointOpt = Optional.empty();
            private Optional<TreeElementType> elementTypeOpt = Optional.empty();
            private Optional<Boolean> successOpt = Optional.empty();
            private Optional<TreeElement> parentOpt = Optional.empty();
            private Optional<TreeElement> leftOpt = Optional.empty();
            private Optional<TreeElement> rightOpt = Optional.empty();
            private Optional<TreeElement> siblingOpt = Optional.empty();

            private TreeElement newElement = null;

            public Builder() {
                this.newElement = new TreeElement();
            }

            public Builder(TreeElement toCopy, boolean copy) {
                this.thisPointOpt = Optional.ofNullable(toCopy.getThisPoint());
                this.elementTypeOpt = Optional.ofNullable(toCopy.getElementType());
                this.parentOpt = Optional.ofNullable(toCopy.getParent());
                this.leftOpt = Optional.ofNullable(toCopy.getLeft());
                this.rightOpt = Optional.ofNullable(toCopy.getRight());
                this.siblingOpt = Optional.ofNullable(toCopy.getSibling());
                this.successOpt = Optional.ofNullable(toCopy.isSuccess());
                if (copy) {
                    this.newElement = new TreeElement();
                } else {
                    this.newElement = toCopy;
                }

            }

            public Builder thisPointOpt(ButtonPoint point) {
                this.thisPointOpt = Optional.of(point);
                return this;
            }

            public Builder elementTypeOpt(TreeElementType type) {
                this.elementTypeOpt = Optional.of(type);
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

            public Builder siblingOpt(TreeElement right) {
                this.siblingOpt = Optional.of(right);
                return this;
            }

            public <T> Builder setSuccessor(Predicate<? super T> predicate, T value) {
               if (predicate != null && value != null) {
                   this.successOpt = Optional.of(predicate.test(value));
               } else {
                   this.successOpt = Optional.of(Boolean.FALSE);
               }
               return this;
            }

            public TreeElement build() {
                TreeElement element = this.newElement;
                element.thisPoint = this.thisPointOpt.orElse(null);
                element.elementType = this.elementTypeOpt.orElse(null);
                element.parent = this.parentOpt.orElse(null);
                element.left = this.leftOpt.orElse(null);
                element.right = this.rightOpt.orElse(null);
                element.sibling = this.siblingOpt.orElse(null);
                element.success = this.successOpt.orElse(Boolean.FALSE);
                return element;
            }

        }
    }

    public enum TreeElementType {
        LEFT("left"),
        RIGHT("right"),
        PARENT("parent"),
        SIBLING("sibling");

        private final String typeName;

        TreeElementType(String typeName) {
            this.typeName = typeName;
        }

        public String getTypeName() {
            return typeName;
        }


    }
}
